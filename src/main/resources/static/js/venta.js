'use strict';
const tbody=document.querySelector('#lineas');
const template=document.querySelector('#linea-template');
function renumerar(){
  tbody.querySelectorAll('tr').forEach((row,i)=>{
    row.querySelector('.producto').name='lineas['+i+'].productoId';
    row.querySelector('.cantidad').name='lineas['+i+'].cantidad';
    row.querySelector('.producto').id='producto-'+i;
    row.querySelector('.cantidad').id='cantidad-'+i;
  });
}
document.querySelector('#agregar').addEventListener('click',()=>{
  if(tbody.querySelectorAll('tr').length>=30){alert('Máximo 30 líneas de venta');return;}
  tbody.append(template.content.cloneNode(true));renumerar();
});
tbody.addEventListener('click',e=>{
  if(e.target.closest('.quitar') && tbody.querySelectorAll('tr').length>1){e.target.closest('tr').remove();renumerar();}
});
document.querySelector('#venta-form').addEventListener('submit',()=>{document.querySelector('#guardar').disabled=true;});
renumerar();
