import{l as S,_ as I}from"./index.e9d54774.js";import{d as k,f as C,o as z,c as B,D as L,aH as W,aI as s,bN as h,H as i,G as a,F as f,aS as l,bO as g,bz as v,cb as p,L as D,b_ as N,K as H,a_ as O,b0 as R}from"./arco.a97fe066.js";const V=k({__name:"index",props:{height:{type:String,default:"100%"},mode:{type:String,default:"fill"},leftWidth:{type:Object,default:()=>({span:4})},leftIsShow:{type:Boolean,default:!0}},emits:["change","update:leftIsShow"],setup(t,{expose:m,emit:x}){const r=t,c=x,o=C(!0);z(()=>{o.value=S.cloneDeep(r.leftIsShow)});const b=B(()=>{const{leftWidth:e}=r;return o.value?{span:24-e.span,xxl:e.xxl?24-e.xxl:void 0,xl:e.xl?24-e.xl:void 0,lg:e.lg?24-e.lg:void 0,md:e.md?24-e.md:void 0,sm:e.sm?24-e.sm:void 0,xs:e.xs?24-e.xs:void 0}:{span:24}}),n=()=>{o.value=!o.value,c("change",o.value),c("update:leftIsShow",o.value)};return m({toggleLeft:n}),(e,$)=>{const y=D,d=N,_=H,u=O,w=R;return L(),W(w,{gutter:10,style:a({position:"relative",height:t.height})},{default:s(()=>[h(i("div",{style:a({height:"100%",left:"16px",width:t.mode==="fill"?"1px":"0px",position:"absolute",zIndex:9,background:"var(--color-border-2)"})},[i("span",{class:f({toggleBtn:!0,trapezoid:t.mode==="space"}),style:a({background:t.mode==="fill"?"var(--color-bg-7)":"var(--color-bg-2)"}),onClick:n},[l(y)],6)],4),[[g,!o.value]]),h(l(u,v(t.leftWidth,{style:{position:"relative"}}),{default:s(()=>[l(d,{style:a({height:t.height,overflow:"auto"})},{default:s(()=>[p(e.$slots,"left",{},void 0,!0)]),_:3},8,["style"]),i("div",{style:a({height:t.height,right:"0px",top:"0px",width:t.mode==="fill"?"1px":"0px",position:"absolute",zIndex:9,background:"var(--color-border-2)"})},null,4),i("span",{class:f({toggleBtn:!0,trapezoid:t.mode==="space"}),style:a({background:t.mode==="fill"?"var(--color-bg-7)":"var(--color-bg-2)"}),onClick:n},[l(_)],6)]),_:3},16),[[g,o.value]]),l(u,v(b.value,{style:{position:"relative",paddingLeft:o.value?"10px":"28px"}}),{default:s(()=>[l(d,{style:a({height:t.height,overflow:"auto"})},{default:s(()=>[p(e.$slots,"right",{},void 0,!0)]),_:3},8,["style"])]),_:3},16,["style"])]),_:3},8,["style"])}}});const G=I(V,[["__scopeId","data-v-21a56bf4"]]);export{G as S};