import{h as t}from"./index.e9d54774.js";function r(e){return t.post("/modelGroup/pageList",e)}function n(e){return t.post("/modelGroup/save",e)}function s(e){return t.get(`/modelGroup/${e}`)}function u(e){return t.post("/modelGroup/checkNameExist",e)}function i(e){return t.delete(`/modelGroup/${e}`)}function p(e){return t.post("/modelGroup/tree",e)}function a(e){return t.post("/modelGroup/treeWithModel",e)}function d(e){return t.post("/modelGroup/list",e||{})}export{p as a,u as c,i as d,s as f,d as l,r as p,n as s,a as t};