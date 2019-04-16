package com.carlgira.soa.util;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
