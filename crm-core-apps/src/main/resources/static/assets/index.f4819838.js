import{a as oe,aD as ue,aE as k,l as ne,aF as le,g as ie}from"./index.e9d54774.js";/* empty css               *//* empty css              *//* empty css               *//* empty css                *//* empty css               */import{d as se,f as p,r as z,o as de,aT as re,D as ce,E as me,H as h,aS as e,aI as u,u as l,aY as g,aW as A,bz as pe,bg as D,n as ge,bq as q,ch as fe,bt as Ce,a_ as _e,aN as Fe,b0 as ve,bf as ye,aM as be,bB as he,aP as De,bo as we,bv as Ee,bp as Be}from"./arco.a97fe066.js";import{p as xe}from"./pagination.72a5f225.js";import{d as P}from"./index.127a6572.js";import"./chart.a235fbdf.js";import"./vue.4eb70094.js";const Ve={class:"page-container"},Se={class:"page-panel"},ke={style:{height:"calc(100vh - 146px)"}},Re=se({__name:"index",setup(ze){const{loading:U,setLoading:w}=oe(!1),{pagination:i,pageSizeOptions:I}=xe(),E=p(),W=p(),G=p({size:"mini",height:"auto",columnConfig:{resizable:!0},showOverflow:"tooltip",columns:[{title:"\u914D\u7F6E\u7C7B\u578B",field:"groupCode",minWidth:120},{title:"\u914D\u7F6E\u540D\u79F0",field:"name",minWidth:120},{title:"\u914D\u7F6E\u7F16\u7801",field:"code",minWidth:120},{title:"\u503C",field:"content",minWidth:120},{title:"\u63CF\u8FF0",field:"description",minWidth:120},{title:"\u72B6\u6001",field:"enable",minWidth:120,slots:{default:"enable"}},{title:"\u521B\u5EFA\u65F6\u95F4",field:"createTime",width:200},{title:"\u64CD\u4F5C",width:120,slots:{default:"action"}}]}),C=z({queryStr:""}),s=async(a={...C,page:i})=>{w(!0);const t=E.value;try{const{data:d}=await ue(a);i.total=d.totalElements,t&&await t.reloadData(d.content)}catch(d){console.error(d)}finally{w(!1)}},T=a=>{i.current=a,s()},$=a=>{i.pageSize=a,s()},L=()=>{s()},r=p(!1),c=p(),n=z({id:"",groupCode:"",name:"",code:"",description:"",enable:!0}),O=async()=>{await c.value.validate()?console.warn("\u9A8C\u8BC1\u4E0D\u901A\u8FC7"):(await k(n),D.success("\u63D0\u4EA4\u6210\u529F"),r.value=!1,await s())},R=a=>{ge(async()=>{try{await k(a),await s(),D.success("\u64CD\u4F5C\u6210\u529F")}catch(t){console.error(t)}})};let _,F;const M=()=>{r.value=!0,c.value.resetFields(),n.id="",_="",F=""},N=a=>{r.value=!0,ne.assign(n,a),_=n.groupCode,F=n.code,c.value.validate()},j=async a=>{q.warning({title:"\u63D0\u793A",titleAlign:"start",hideCancel:!1,content:`\u6B64\u64CD\u4F5C\u5C06\u6C38\u4E45\u5220\u9664\u914D\u7F6E'${a.name}'\uFF0C\u662F\u5426\u7EE7\u7EED?`,onOk:async()=>{await le(a.id),D.success("\u5220\u9664\u6210\u529F"),await s()}})},B=P(async()=>{await s()},500);de(()=>{s()});let v=!1;const H=a=>{const{value:t}=a.target;v=v||!!t},Y=a=>{a!==_&&v&&n.code&&c.value.validateField("code")},J=P(async(a,t)=>{if(a!==F&&n.groupCode){const{data:d}=await ie(n.groupCode,a);d&&t("\u8BE5\u914D\u7F6E\u5DF2\u5B58\u5728")}},500);return(a,t)=>{const d=fe,y=Ce,x=_e,K=Fe,Q=ve,X=ye,V=be,Z=he,ee=re("vxe-grid"),b=De,m=we,S=Ee,te=Be,ae=q;return ce(),me("div",Ve,[h("div",Se,[e(Q,null,{default:u(()=>[e(x,{span:14},{default:u(()=>[e(y,{style:{"margin-bottom":"5px"}},{default:u(()=>[e(d,{modelValue:C.queryStr,"onUpdate:modelValue":t[0]||(t[0]=o=>C.queryStr=o),style:{width:"320px"},placeholder:"\u914D\u7F6E\u7C7B\u578B/\u540D\u79F0/\u7F16\u7801","allow-clear":"",onSearch:L,onInput:l(B),onClear:l(B)},null,8,["modelValue","onInput","onClear"])]),_:1})]),_:1}),e(x,{span:10,style:{"text-align":"right"}},{default:u(()=>[e(y,{style:{"margin-bottom":"5px"}},{default:u(()=>[e(K,{type:"primary",size:"medium",onClick:M},{default:u(()=>[g(" \u65B0\u5EFA ")]),_:1})]),_:1})]),_:1})]),_:1}),h("div",ke,[e(ee,pe({ref_key:"xGrid",ref:E,loading:l(U)},G.value),{index:u(({rowIndex:o})=>[g(A(o+1+(l(i).current-1)*l(i).pageSize),1)]),enable:u(({row:o})=>[e(X,{modelValue:o.enable,"onUpdate:modelValue":f=>o.enable=f,type:"line",onClick:f=>R(o)},null,8,["modelValue","onUpdate:modelValue","onClick"])]),action:u(({row:o})=>[e(y,null,{default:u(()=>[e(V,{onClick:f=>N(o)},{default:u(()=>[g("\u7F16\u8F91")]),_:2},1032,["onClick"]),e(V,{type:"text",onClick:f=>j(o)},{default:u(()=>[g("\u5220\u9664")]),_:2},1032,["onClick"])]),_:2},1024)]),pager:u(()=>[e(Z,{ref_key:"xPagination",ref:W,current:l(i).current,"page-size":l(i).pageSize,total:l(i).total,"page-size-options":l(I),style:{"padding-top":"10px"},"show-total":"","show-jumper":"","show-page-size":"",onChange:T,onPageSizeChange:$},null,8,["current","page-size","total","page-size-options"])]),_:1},16,["loading"])])]),e(ae,{visible:r.value,width:"auto","title-align":"start","mask-closable":!1,draggable:"",onOk:O,onCancel:t[6]||(t[6]=o=>r.value=!1)},{title:u(()=>[g(A(n.id?"\u7F16\u8F91\u914D\u7F6E":"\u65B0\u5EFA\u914D\u7F6E"),1)]),default:u(()=>[h("div",null,[e(te,{ref_key:"submitFormRef",ref:c,model:n,style:{width:"600px"}},{default:u(()=>[e(m,{field:"groupCode",label:"\u914D\u7F6E\u7C7B\u578B",rules:[{required:!0,message:"\u914D\u7F6E\u7C7B\u578B\u5FC5\u586B"},{maxLength:64,message:"\u914D\u7F6E\u7C7B\u578B\u957F\u5EA6\u6700\u5927\u4E0D\u80FD\u8D85\u8FC764"},{validator:Y}],"validate-trigger":"blur"},{default:u(()=>[e(b,{modelValue:n.groupCode,"onUpdate:modelValue":t[1]||(t[1]=o=>n.groupCode=o),onBlur:H},null,8,["modelValue"])]),_:1},8,["rules"]),e(m,{field:"name",label:"\u914D\u7F6E\u540D\u79F0",rules:[{required:!0,message:"\u914D\u7F6E\u540D\u79F0\u5FC5\u586B"}],"validate-trigger":"blur"},{default:u(()=>[e(b,{modelValue:n.name,"onUpdate:modelValue":t[2]||(t[2]=o=>n.name=o)},null,8,["modelValue"])]),_:1}),e(m,{field:"code",label:"\u7F16\u7801",rules:[{required:!0,message:"\u7F16\u7801\u5FC5\u586B"},{validator:l(J)}],"validate-trigger":"blur"},{default:u(()=>[e(b,{modelValue:n.code,"onUpdate:modelValue":t[3]||(t[3]=o=>n.code=o)},null,8,["modelValue"])]),_:1},8,["rules"]),e(m,{field:"content",label:"\u503C"},{default:u(()=>[e(S,{modelValue:n.content,"onUpdate:modelValue":t[4]||(t[4]=o=>n.content=o),"show-word-limit":""},null,8,["modelValue"])]),_:1}),e(m,{field:"description",label:"\u63CF\u8FF0"},{default:u(()=>[e(S,{modelValue:n.description,"onUpdate:modelValue":t[5]||(t[5]=o=>n.description=o),"show-word-limit":"","max-length":500},null,8,["modelValue"])]),_:1})]),_:1},8,["model"])])]),_:1},8,["visible"])])}}});export{Re as default};