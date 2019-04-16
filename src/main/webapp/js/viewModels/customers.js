/**
 * @license
 * Copyright (c) 2014, 2019, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/*
 * Your customer ViewModel code goes here
 */
define(['ojs/ojcore', 'knockout', 'jquery', 'pako', 'ojs/ojarraydataprovider', 'ojs/ojselectcombobox', 'ojs/ojtable', 'ojs/ojbutton', 'ojs/ojinputtext'],
 function(oj, ko, $, pako, ArrayDataProvider) {

    function CustomerViewModel() {
      var self = this;
      self.type = ko.observable();
      self.id = ko.observable();
      self.image = ko.observable();
      self.diagram = ko.observable();
      self.auditType = ko.observable();
      self.bpmAuditText = ko.observable("");

      self.selectedItems = ko.observable([]);
      self.dataprovider = ko.observable();
      self.auditEvents = [];
      self.auditEventDetail = ko.observable();

      // Below are a set of the ViewModel methods invoked by the oj-module component.
      // Please reference the oj-module jsDoc for additional information.

      /**
       * Optional ViewModel method invoked after the View is inserted into the
       * document DOM.  The application can put logic that requires the DOM being
       * attached here.
       * This method might be called multiple times - after the View is created
       * and inserted into the DOM and after the View is reconnected
       * after being disconnected.
       */

       $.urlParam = function (name) {
           var results = new RegExp('[\?&]' + name + '=([^&#]*)')
                             .exec(window.location.search);

           return (results !== null) ? results[1] || 0 : false;
       }

       self.type($.urlParam('type'));
       self.id($.urlParam('id'));

       self.bpmFlagImg = ko.observable(self.type() === 'bpmn');
       self.bpmFlagText = ko.observable(self.type() === 'bpmn' && !self.bpmFlagImg());
       self.bpelFlag = self.type() === 'bpel';

      if(self.bpmFlagImg()){
          self.diagram = "/soa-services/audit/bpm/audit/image.png?cikey=" + self.id();
      }

      if(self.bpelFlag){


        $.ajax({
          "async": true,
          "method": "GET",
          "crossDomain": true,
          "url": "/soa-services/audit/bpel/text?cikey=" + self.id(),
          "headers": {
              "Content-Type": "application/json",
              "Accept": "application/json",
              "cache-control": "no-cache"
          }
          }).then(function(data) {
            console.log(JSON.stringify(data));
          },
          function( jqXHR, textStatus, errorThrown ) {
              console.log("ERROR");
          });
      }

      function uncompressData(b64Data){
          var strData = atob(b64Data);
          var charData = strData.split('').map(function(x){return x.charCodeAt(0);});
          var binData = new Uint8Array(charData);
          var data = pako.inflate(binData);
          var strData = String.fromCharCode.apply(null, new Uint16Array(data));

          return strData;
      };

      self.valueChangeHandler = function (event) {
        var value = event.detail.value;

        if(value == 'text' && self.bpmFlagImg()){
          self.bpmFlagImg(false);
          self.bpmFlagText(true);


          $.ajax({
            "async": true,
            "method": "GET",
            "crossDomain": true,
            "url": "/soa-services/audit/bpm/text?cikey=" + self.id(),
            "headers": {
                "Content-Type": "application/json",
                "Accept": "application/json",
                "cache-control": "no-cache"
            }
            }).then(function(data) {
              self.bpmAuditText(JSON.stringify(data));

              self.auditEvents = [];
              var counter = 0;
              data.forEach(function(e) {
                var c = JSON.parse(JSON.stringify(e));
                c['id'] = counter;
                if(c["auditLog"] != null){
                    c["auditLog"] = uncompressData(c["auditLog"]);
                }
                self.auditEvents.push(c);
                counter++;
               });

              self.dataprovider(new ArrayDataProvider(self.auditEvents, {keyAttributes: 'id'} ));
            },
            function( jqXHR, textStatus, errorThrown ) {
                console.log("ERROR");
            });
        }

        if(value == 'image' && self.bpmFlagText()){
          self.bpmFlagImg(true);
          self.bpmFlagText(false);
        }

      };

      self.selectionListener = function(event) {
        if(self.selectedItems().length > 0){
          var id = self.selectedItems()[0].startKey.row;

          parser = new DOMParser();
          xmlDoc = parser.parseFromString(self.auditEvents[id]["auditLog"],"text/xml");


          self.auditEventDetail(JSON.stringify(self.auditEvents[id]["auditLog"]));
          self.selectedItems([]);
          var popup = document.getElementById('popup1');
          popup.open();
        }
      };

      self.cancelFlowListener = function(event){
        var popup = document.getElementById('popup1');
        popup.close();
      };

      self.connected = function() {
        // Implement if needed
      };

      // /audit/bpm/text

      /**
       * Optional ViewModel method invoked after the View is disconnected from the DOM.
       */
      self.disconnected = function() {
        // Implement if needed
      };

      /**
       * Optional ViewModel method invoked after transition to the new View is complete.
       * That includes any possible animation between the old and the new View.
       */
      self.transitionCompleted = function() {
        // Implement if needed
      };
    }

    /*
     * Returns a constructor for the ViewModel so that the ViewModel is constructed
     * each time the view is displayed.  Return an instance of the ViewModel if
     * only one instance of the ViewModel is needed.
     */
    return new CustomerViewModel();
  }
);
