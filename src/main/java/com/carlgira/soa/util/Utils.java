package com.carlgira.soa.util;

import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

/**
 * Created by carlgira on 08/03/2016.
 */
public class Utils {

    public static void marshall(Object object, OutputStream outputStream){
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(object, outputStream );
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static Object unmarshallString(String xmlContent, Class oClass ){
        Object result = null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(oClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlContent);
            result = jaxbUnmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String uncompressString(String zippedBase64Str) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(zippedBase64Str);

        ByteArrayInputStream bais = new ByteArrayInputStream(decodedBytes);
        GZIPInputStream gzis = new GZIPInputStream(bais);
        InputStreamReader reader = new InputStreamReader(gzis);
        BufferedReader in = new BufferedReader(reader);

        String readed;
        StringBuilder builder = new StringBuilder();
        while ((readed = in.readLine()) != null) {
            builder.append(readed);
        }

        return builder.toString();
    }

    private static void writeXmlDocumentToXmlFile(Document xmlDocument)
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();

            // Uncomment if you do not require XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            //A character stream that collects its output in a string buffer,
            //which can then be used to construct a string.
            StringWriter writer = new StringWriter();

            //transform document to string
            transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));

            String xmlString = writer.getBuffer().toString();
            System.out.println(xmlString);                      //Print to console or logs
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static ServerConnection getServerConnection(HttpServletRequest request){
        String userAuth = request.getHeader("Authorization");

        if(userAuth == null){
            userAuth = request.getSession().getAttribute("Authorization").toString();
        }
        else if(request.getSession().getAttribute("Authorization") == null){
            request.getSession().setAttribute("Authorization", userAuth);
        }

        byte[] decodedBytes = Base64.getDecoder().decode(userAuth.split(" ")[1]);
        String authorization = new String(decodedBytes);
        String user = authorization.split(":")[0];
        String password = authorization.split(":")[1];

        return new ServerConnection("t3://" + request.getLocalName() + ":" + request.getLocalPort() + "/soa-infra/", user, password,"");
    }
}
