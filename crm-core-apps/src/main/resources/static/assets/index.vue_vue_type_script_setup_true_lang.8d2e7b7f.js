import{l as d}from"./index.e9d54774.js";import{d as f,f as _,c as o,D as v,E as h,aS as n,aI as r,aY as y,aW as k,bi as D}from"./arco.a97fe066.js";import{_ as b}from"./performance-data.vue_vue_type_script_setup_true_lang.419c45ec.js";const g=f({__name:"index",props:{visible:{type:Boolean,default:!1},appKeyItemIds:{}},emits:["cancel"],setup(w,{emit:l}){const c=l,i=()=>{c("cancel")},t=_({itemIsNull:!1,showGraph:!0,graph:[],text:[]}),p=o(()=>{var e;return d.uniq((e=t.value)==null?void 0:e.text.map(a=>a.appKey+a.hostid)).length}),s=o(()=>{var e,a;return p.value===1?(a=(e=t.value)==null?void 0:e.text[0])==null?void 0:a.displayName:""}),m=e=>{t.value=e};return(e,a)=>{const u=D;return v(),h("div",null,[n(u,{width:"65vw",visible:e.visible,"mask-closable":!0,footer:!1,onCancel:i},{title:r(()=>[y(" \u6027\u80FD\u6570\u636E"+k(s.value?`[${s.value}]`:""),1)]),default:r(()=>[n(b,{"app-key-item-ids":e.appKeyItemIds,onGetPerformanceData:m},null,8,["app-key-item-ids"])]),_:1},8,["visible"])])}}});export{g as _};