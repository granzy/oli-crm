import{h as A,a as O,l as _}from"./index.e9d54774.js";/* empty css               *//* empty css                */import{d as R,f as a,o as T,aT as H,D as K,aH as V,aI as h,aY as C,aW as W,u as m,H as b,aS as w,bz as j,bB as q,aN as G,bt as M}from"./arco.a97fe066.js";import{E as J,F as U}from"./FileSaver.min.64c4208e.js";import{c as F}from"./date-time.3a778351.js";import{p as Y}from"./pagination.72a5f225.js";function $(n){return A.post("/zabbixAcknowledge/acknowledgeList",n)}const z=[{header:"\u4E3B\u673A",key:"hostName",width:40},{header:"\u4E8B\u4EF6",key:"eventName",width:60},{header:"\u7528\u6237",key:"username",width:30},{header:"\u7C7B\u578B",key:"actionName",width:30},{header:"\u65F6\u95F4",key:"clock",width:40},{header:"\u5E94\u7528",key:"appName",width:30}],Q=n=>{const s=new J.Workbook;s.creator="UMOP",s.keywords="acknowledge";const l=s.addWorksheet("\u5904\u7406\u8BB0\u5F55");l.columns=z;const c=l.getRow(1);c.height=20,c.eachCell(e=>{e.font={name:"\u9ED1\u4F53",bold:!0,size:14,color:{argb:"FF1C1C1C"}},e.alignment={vertical:"middle",horizontal:"center"}}),n.forEach(e=>{e.clock=F(e.clock),l.addRow(e)});const f=1,p=n.length+1,i=1,r=z.length;for(let e=f;e<=p;e+=1)for(let o=i;o<=r;o+=1){const u=l.getCell(e,o);u.border={top:{style:"thin"},left:{style:"thin"},bottom:{style:"thin"},right:{style:"thin"}},u.alignment={vertical:"middle",horizontal:"center"}}s.xlsx.writeBuffer().then(e=>{const o=new Blob([e],{type:"application/octet-stream"});U.saveAs(o,"\u5904\u7406\u8BB0\u5F55.xlsx")})},X={style:{display:"flex",width:"100%","padding-top":"10px","justify-content":"space-between"}},le=R({__name:"zabbix-acknowledge-list",props:{appKey:{type:String,required:!0},eventId:{type:String,required:!0}},setup(n,{expose:s}){const{loading:l,setLoading:c}=O(!1),{pageSizeOptions:f,pagination:p}=Y(),i=a(p.current),r=a(p.pageSize),e=a(),o=a(),u=n,B=a({size:"mini",height:"auto",columnConfig:{resizable:!0},rowConfig:{keyField:"acknowledgeid",isHover:!0},showOverflow:"tooltip",columns:[{title:"\u7528\u6237",field:"username",width:180},{title:"\u64CD\u4F5C",field:"actionName"},{title:"\u6D88\u606F",field:"message"},{title:"\u65F6\u95F4",field:"clock",width:160,slots:{default:"clock"}}]}),d=a([]),k=a([]),S=a([]),v=()=>{const t=(i.value-1)*r.value,g=t+r.value;k.value=_.cloneDeep(d.value.slice(t,g))},y=async()=>{c(!0);try{d.value=(await $({appKey:u.appKey,eventId:u.eventId})).data,S.value=_.cloneDeep(d.value),v()}catch{}finally{c(!1)}},D=t=>{i.value=t,v()},N=t=>{i.value=1,r.value=t,v()};return T(()=>{y()}),s({fetchData:y}),(t,g)=>{const E=q,P=G,I=M,L=H("vxe-grid");return K(),V(L,j({ref_key:"xGrid",ref:e},B.value,{loading:m(l),data:k.value}),{clock:h(({row:x})=>[C(W(m(F)(x.clock)),1)]),pager:h(()=>[b("div",X,[w(E,{ref_key:"xPagination",ref:o,total:d.value.length,current:i.value,"page-size":r.value,"page-size-options":m(f),"show-total":"","show-jumper":"","show-page-size":"",onChange:D,onPageSizeChange:N},null,8,["total","current","page-size","page-size-options"]),b("div",null,[w(I,null,{default:h(()=>[w(P,{onClick:g[0]||(g[0]=x=>m(Q)(d.value))},{default:h(()=>[C("\u5BFC\u51FA")]),_:1})]),_:1})])])]),_:1},16,["loading","data"])}}});export{le as _};