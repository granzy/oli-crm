import{L as o,M as c,h as n}from"./index.e9d54774.js";import{c as i}from"./color.562bf6e9.js";function l(e){return n.post("/configBackupDevice/pageList",e)}function g(e){return n.post("/configBackupDevice/findAllByDisplayNameAndIpList",e)}function d(e){return n.post("/configBackupDevice",e)}function C(e){return n.post("/configBackupDevice/updateEnable",e)}function B(e){return n.post("/configBackupDevice/viewConfigFileByExecDetails",e)}function D(e){return n.post("/configBackupDevice/treeListGroupByConfigBackupDeviceSeries",e)}function E(e){return{SUCCESS:"\u6210\u529F",FAILURE:"\u5931\u8D25",RUNNING:"\u8FDB\u884C\u4E2D",FINISH:"\u90E8\u5206\u5931\u8D25"}[e]}const u=i("success-6"),r=i("info-6"),s=i("danger-6"),a=i("warning-6");function I(e){return{SUCCESS:u,FAILURE:s,RUNNING:r,FINISH:a}[e]}function y(e){if(!e)return e;if(e.indexOf("Read timed out")>-1)return"\u8FDE\u63A5\u8D85\u65F6";e=e.replace("java.lang.RuntimeException:","");const t=e.split(`
`);return t.length>0?t[0]:e}const v={sysObjectId:"",manufacturerCode:"",deviceType:"",deviceModel:"",publicAccountId:"",certificateType:o.GLOBAL,connectType:c.SSH,loginAccount:"",loginPassword:"",loginPort:22};function A(e){return n.get(`/configBackupDevice/getVisiblePassword/${e}`)}function S(e,t){return n.get(`/configBackupDevice/getSysObjectIdByAppKeyAndHostId/${e}/${t}`)}export{A as a,I as b,E as c,v as d,g as e,y as f,S as g,l as q,d as s,D as t,C as u,B as v};