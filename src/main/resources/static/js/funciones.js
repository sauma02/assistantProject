/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */

function carga_ajax_get(ruta, valor, div){
    $.get(ruta, {valor: valor}, function(resp){
        $("#" + div + " ").html(resp);
    });
    return false;
}

