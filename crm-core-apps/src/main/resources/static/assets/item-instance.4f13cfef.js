import{h as n}from"./index.e9d54774.js";function o(t){return n.post("/itemInstance/pageList",t)}function r(t){return n.post("/itemInstance/list",t)}function c(t){return n.post("/itemInstance/findItemInstanceByAppKeyItemIds",{appKeyItemIds:t})}function i(){return n.post("/itemInstance/zabbixHostAndHostGroupSync")}function u(){return n.post("/itemInstance/zabbixItemInstanceSync")}function I(t){return n.post("/itemInstance/performanceDataView",t)}var a=(t=>(t.ZabbixAgent="0",t.ZabbixTrapper="2",t.SimpleCheck="3",t.ZabbixInternal="5",t.ZabbixAgentActive="7",t.WebItem="9",t.ExternalCheck="10",t.DatabaseMonitor="11",t.IPMIAgent="12",t.SSHAgent="13",t.TelnetAgent="14",t.Calculated="15",t.JMXAgent="16",t.SNMPTrap="17",t.DependentItem="18",t.HTTPAgent="19",t.SNMPAgent="20",t.Script="21",t))(a||{});function f(t,e){return n.post(`/itemInstance/taskCreate/${t}/${e}`)}function d(t){return n.post("/itemInstance/downloadTextData",t,{responseType:"blob",timeout:36e5})}function g(t,e){return n.get(`/itemInstance/getZabbixItemCountDataDtoList/${t}/${e}`)}export{a as Z,o as a,u as b,f as c,d,c as f,g,I as p,r as q,i as z};
