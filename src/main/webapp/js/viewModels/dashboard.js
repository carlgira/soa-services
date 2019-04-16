/**
 * @license
 * Copyright (c) 2014, 2019, Oracle and/or its affiliates.
 * The Universal Permissive License (UPL), Version 1.0
 */
/*
 * Your dashboard ViewModel code goes here
 */

define(['ojs/ojcore', 'knockout', 'jquery', 'ojs/ojarraydataprovider', 'ojs/ojtable', 'ojs/ojselectcombobox', 'ojs/ojinputtext', 'ojs/ojlabel', 'ojs/ojradioset',
        'ojs/ojdatetimepicker', 'ojs/ojtimezonedata', 'ojs/ojtreeview', 'ojs/ojjsontreedatasource', 'ojs/ojbutton', 'ojs/ojdialog',
      'ojs/ojinputnumber', 'ojs/ojdatagrid', 'ojs/ojrowexpander', 'ojs/ojflattenedtreedatagriddatasource', 'ojs/ojflattenedtreetabledatasource'],
 function(oj, ko, $ ,ArrayDataProvider) {

    function DashboardViewModel() {
      var self = this;

      //self.dataprovider = new ArrayDataProvider([], {keyAttributes: 'id', implicitSort: [{attribute: 'id', direction: 'ascending'}]});

      self.dataprovider = ko.observable();
      self.searchIdName = ko.observable("flowid");
      self.searchIdValue = ko.observable("");
      self.dateFilterName = ko.observable("minute");
      self.dateFilterValue = ko.observable("15");
      self.dateFilterType = ko.observable("relative");
      self.compositesTree = ko.observable();

      self.compositeSelection = ko.observable("all");
      self.searchIdSensorName = ko.observable();
      self.disabledDate1 = ko.observable(false);
      self.disabledDate2 = ko.observable(true);

      self.sensorListNames = ko.observable();

      self.compositeState = ko.observable("all");
      self.compositeFault = ko.observable("all");

      self.disableSearch  = ko.observable(false);
      self.disabledEcid = ko.observable(true);

      self.startDateTime = ko.observable();
      self.endDateTime = ko.observable();
      self.deployedComposites = null;
      self.compositeName = ko.observable();

      self.flowDataTree = ko.observable();
      self.flowTreeData = ko.observable();
      self.selectedItems = ko.observable([]);

      self.flowTree = {};


      self.compositeHandleOpen = function() {
        document.querySelector("#scrollingDialog").open();
      };

      var flowDataTreeOptions = {
      'expanded': 'all',
      'rowHeader': 'name',
      'columns': ['cikey', 'type', 'state', 'modifyDate']
      };

      self.compositeHandleClose = function() {
        document.querySelector("#scrollingDialog").close();
      };

      self.compositeAction = function(event){
        return true;
      };

      self.openAuditData = function(event){

        return true;
      };

      self.chooseTypeDate = function(event){
          var disabledDate1 = self.disabledDate1();
          var disabledDate2 = self.disabledDate2();
          self.disabledDate1(!disabledDate1);
          self.disabledDate2(!disabledDate2);
        };

        self.chooseTypeId = function(event){
            var value = self.searchIdName();
            if(value === "sensor"){
              self.disabledEcid(self.compositeSelection() == "all" || self.compositeSelection().split("/").length != 3);
              //self.disabledEcid(false);
            }
            else{
              self.disabledEcid(true);
            }
        };

        self.cancelFlowListener = function(event){
          var popup = document.getElementById('popup1');
          popup.close();
        };

/*
        $(document).ready
        (
           function()
           {
              var table = document.getElementById('table');
              table.addEventListener('ojBeforeCurrentRow', self.selectionListener);
           }

        );
        */

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
      self.connected = function() {

        self.filterComposites = function()
        {
            var filter = document.getElementById('filter').rawValue.toLowerCase();
            if (filter.length == 0)
            {
                self.clearFilter();
                return;
            }

            function filterFlowTree(tree){
              var jtree = [];

              tree.forEach(function(e) {
                if(e.hasOwnProperty('children') && e.children.length > 0){
                  jtree = jtree.concat(filterFlowTree(JSON.parse(JSON.stringify(e.children))));
                }

                if((e.attr.name != null && e.attr.name.toLowerCase().includes(filter)) ||
                  (e.attr.state != null && e.attr.state.toLowerCase().includes(filter)) ||
                  (e.attr.type != null &&  e.attr.type.toLowerCase().includes(filter)) ||
                  (e.attr.modifyDate != null &&  e.attr.modifyDate.toLowerCase().includes(filter)) ||
                  (e.attr.cikey != null &&  e.attr.cikey.toString(10).toLowerCase().includes(filter))) {
                  jtree.push(e);
                }
              });

               return jtree;
            };

            var filtredTree = filterFlowTree(JSON.parse(JSON.stringify(self.flowTree)));
            filtredTree.reverse();

            self.flowDataTree(new oj.FlattenedTreeTableDataSource(new oj.FlattenedTreeDataSource(new oj.JsonTreeDataSource(filtredTree), flowDataTreeOptions)));
        };

        self.clearFilter = function(event){
            self.flowDataTree(new oj.FlattenedTreeTableDataSource(new oj.FlattenedTreeDataSource(new oj.JsonTreeDataSource(self.flowTree), flowDataTreeOptions)));
            document.getElementById('filter').value = "";
            return true;
        };

                self.selectionListener = function(event) {
                  var flowid = self.selectedItems()[0].startKey.row;

                  self.selectedItems([]);

                  $.ajax({
                    "async": true,
                    "method": "GET",
                    "crossDomain": true,
                    "url":  "/soa-services/audit/flow?flowid="+ flowid,
                    "headers": {
                        "Content-Type": "application/json",
                        "Accept": "application/json",
                        "cache-control": "no-cache"
                    }
                    }).then(function(data) {

                      function createFlowTree(tree){
                        var jtree = [];

                        tree.forEach(function(e) {
                          var node = {"attr":{"id": e.cikey, "cikey" : e.cikey ,"name": e.partition + "/" + e.name + "/" + e.revision, "state" : e.state, "type" : e.type, "modifyDate" : e.modifyDate }, "children" : createFlowTree(e.components)};

                          e.tasks.forEach(function(t) {
                            var task = {"attr":{"id": t.tasknumber, "cikey" : t.cikey , "name": "t.name", "state" : t.state, "type" : "task", "modifyDate" : e.updateDate }};
                            node.children.push(task);
                           });
                          jtree.push(node);
                         });
                         return jtree;
                      };

                      self.flowTree = createFlowTree(data);

                      self.flowDataTree(new oj.FlattenedTreeTableDataSource(new oj.FlattenedTreeDataSource(new oj.JsonTreeDataSource(self.flowTree), flowDataTreeOptions)));
                    },
                    function( jqXHR, textStatus, errorThrown ) {
                        self.flowDataTree(new oj.FlattenedTreeTableDataSource(new oj.FlattenedTreeDataSource(new oj.JsonTreeDataSource([]), [])));
                    });

                  var popup = document.getElementById('popup1');
                  popup.open();
                };

        self.compositesTreeSelectionChanged = function(event) {
          var newSelection = event.detail.value;
          self.compositeSelection(newSelection.length > 0 ? newSelection[0].split(" ")[0] : 'all')

          if(self.searchIdName() == "sensor"){
            self.disabledEcid(false);
          }
        };

        self.searchFilterAction = function(event) {

          var validations = true;
          if(self.compositeSelection() != "all" && self.compositeSelection().split("/").length != 3 ){
            alert("Choose a real composite, domain/name/revision");
            validations = false;
          }

          if(!self.disabledDate1() && isNaN(self.dateFilterValue())){
            alert("Date value is not a number");
            validations = false;
          }

          if(!self.disabledDate2() && (typeof self.startDateTime() === 'undefined' || typeof self.endDateTime() === 'undefined' )){
            alert("Date value required");
            validations = false;
          }

          if(validations){

            var filterRequest = {
              "state" : self.compositeState(),
              "fault" : self.compositeFault(),
              "page": 0,
              "size": 100
            };

            if(self.compositeSelection() != "all"){
              var compositeInfo = self.compositeSelection().split("/");
              filterRequest["partition"] = compositeInfo[0];
              filterRequest["composite"] = compositeInfo[1];
              filterRequest["revision"] = compositeInfo[2];
            }

            if(self.searchIdValue().length > 0){
              if(self.searchIdName() == "flowid"){
                filterRequest["flowids"] = [self.searchIdValue()];
              }

              if(self.searchIdName() == "cikey"){
                filterRequest["cikeys"] = [self.searchIdValue()];
              }

              if(self.searchIdName() == "ecid"){
                filterRequest["ecids"] = [self.searchIdValue()];
              }

              if(self.searchIdName() == "sensor"){
                filterRequest["sensorName"] = self.searchIdSensorName();
                filterRequest["sensorValues"] = [self.searchIdValue()];
              }
            }

            if(!self.disabledDate1()){
              var timecalc = 0;

              if(self.dateFilterName() == "minute"){
                timecalc = self.dateFilterValue() * 60 * 1000;
              }

              if(self.dateFilterName() == "hour"){
                timecalc = self.dateFilterValue()* 60 * 60 * 1000;
              }

              if(self.dateFilterName() == "day"){
                timecalc = self.dateFilterValue()* 24 * 60 * 60 * 1000;
              }

              if(self.dateFilterName() == "week"){
                timecalc = self.dateFilterValue()* 7 * 24 * 60 * 60 * 1000;
              }

              if(self.dateFilterName() == "month"){
                timecalc = self.dateFilterValue()* 30 * 24 * 60 * 60 * 1000;
              }

              filterRequest["startDate"] = new Date(new Date(new Date().toString().split('GMT')[0]+' UTC').getTime() - timecalc).toISOString().split('.')[0];
              filterRequest["endDate"] = new Date(new Date().toString().split('GMT')[0]+' UTC').toISOString().split('.')[0];
            }

            if(!self.disabledDate2()){
              filterRequest["startDate"] = self.startDateTime();
              filterRequest["endDate"] = self.endDateTime();
            }

            $.ajax({
              "async": true,
              "method": "POST",
              "crossDomain": true,
              "url": "/soa-services/composites",
              "headers": {
                  "Content-Type": "application/json",
                  "Accept": "application/json",
                  "cache-control": "no-cache"
              },
              "data": JSON.stringify(filterRequest)
              }).then(function(data) {
                self.disableSearch(false);
                 var lcomposites = [];

                 data.composites.forEach(function(e) {
                    c = e;
                    c["state"] = e.activeComponents > 0 ? "Running" : "Terminated";
                    c["fault"] = e.unhandledFaults + e.recoverableFaults;
                    lcomposites.push(c);
                  });
                  self.dataprovider(new ArrayDataProvider(lcomposites, {keyAttributes: 'flowid', implicitSort: [{attribute: 'flowid', direction: 'ascending'}]}));
              });
          }
        }

        $.ajax({
          "async": true,
          "method": "GET",
          "crossDomain": true,
          "url": "/soa-services/sensor/names",
          //"url": "http://localhost:7001/soa-services/sensor/names",
          "headers": {
              "Content-Type": "application/json",
              "Accept": "application/json"
          }
          }).then(function(data) {

            var sensorList = [];
            data.forEach(function(e) {
                sensorList.push({value: e, label: e});
            });

            self.sensorListNames(new ArrayDataProvider(sensorList, {keyAttributes: 'value'}));
          });

          $.ajax({
            "async": true,
            "method": "GET",
            "crossDomain": true,
            "url": "/soa-services/deployed-composites",
            "headers": {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
            }).then(function(data) {
               var tempComposites = {};
               var tempTree = [{"attr":{"title": "all" ,"id": "all" }}];

               data.deployedComposites.forEach(function(e) {
                 if(typeof tempComposites[e.partition] === 'undefined') {
                    tempComposites[e.partition] = {};
                    tempComposites[e.partition][e.name] = []
                    tempComposites[e.partition][e.name].push(e.revision + (e.default ? " (default)" : "") );
                  }
                  else {
                    if(typeof tempComposites[e.partition][e.name] === 'undefined') {
                       tempComposites[e.partition][e.name] = []
                       tempComposites[e.partition][e.name].push(e.revision + (e.default ? " (default)" : "") );
                     }
                     else {
                         tempComposites[e.partition][e.name].push(e.revision + (e.default ?  " (default)" : "") );
                     }
                  }
                });

                for(var p in tempComposites){
                    var t = {"attr":{"title": p ,"id": p },"children":[]};
                    for(var n in tempComposites[p]){
                        var tt = {"attr":{"title": n ,"id": n },"children":[]};
                        tempComposites[p][n].forEach(function(v) {
                          var ttt = {"attr":{"title": v ,"id": p + "/" + n + "/" + v}};
                          tt["children"].push(ttt);
                        });
                        t["children"].push(tt);
                    }
                    tempTree.push(t);
                }

                self.compositesTree(new oj.JsonTreeDataSource(tempTree));
                });

      };

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
    return new DashboardViewModel();
  }
);
