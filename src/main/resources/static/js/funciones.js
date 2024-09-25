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
function descargarArchivos(nombreCandidato){
    fetch("/listaCandidatos/"+nombreCandidato+"/descargar-archivos", {
        method:"GET"
    })
            .then(response => {
                if(!response.ok){
                    throw new Error("Error al descargar archivos");
                }
                return response.blob();
    })
            .then(blob =>{
                var url = window.URL.createObjectUrl(blob);
                var a = document.createElement('a');
                a.href=url;
                a.download = nombreCandidato + "_archivos.zip";
                document.body.appendChild(a);
                a.click();
                a.remove();
                
    })
            .catch(error => {
                console.error("Error: ", error);
    });
}

