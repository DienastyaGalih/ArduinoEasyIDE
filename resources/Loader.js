/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


$(document).ready(function () {
    console.log("document loaded");
    var jqxhr = $.get("http://localhost:8080/project/get/budi", function (data) {


        $("#projectName").text("~" + data.project.name);
        $("#arduinoType").text(data.project.arduino_type);
        $("#arduinoIC").text(data.project.arduino_IC);

        for (var i = 0; i < data.folders.length; i++) {
            addFolder(data.folders[i]);
        }


    }).done(function () {
        alert("second success " + data);
    }).fail(function () {
        alert("error");
    }).always(function () {
        alert("finished");
    });
});
$(window).load(function () {

});

function addFolder(folder) {
    var filesText="";
    for(var i=0;i<folder.files.length;i++){
        
    }
    
    $("#projectBar").append('<li class="node" ><span class="leaf"><span class="mif-folder"></span> ' + folder.name + '</span><span class="node-toggle"></span><ul></ul></li>');
}



function addFile(file){
    
}


function specialClick() {
    var charm = $("#menu-special").data("charm");
    if (charm.element.data("opened") === true) {
        charm.close();
    } else {
        charm.open();
    }
}
