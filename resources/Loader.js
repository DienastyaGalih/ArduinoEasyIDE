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


//        alert("dekkp smpeisd "+data.project_structure.files[0].name);
        addProject(data.project_structure);
//        for (var i = 0; i < data.folders.length; i++) {
//            addFolder(data.folders[i]);
//        }


    }).done(function () {
        alert("second success ");
    }).fail(function () {
        alert("error");
    }).always(function () {
        alert("finished");
    });


    $("#virifyButton").click(function (event) {


//        var $f = $("#frame_lcdhdata");
//        var valueEditor = $f[0].contentWindow.getvalueEditor();  //works
//        alert('bahaya');
//        valueEditor = $f.get(0).contentWindow.getvalueEditor(); //works    
//        alert(valueEditor);



        var $f = $("#frameCode");
////        var valueEditor = $f[0].contentWindow.getvalueEditor();  //works
        $f.get(0).contentWindow.setValueEditor("makan"); //works    

//        var $f = $("#frameCode");
//        var valueEditor = $f[0].contentWindow.getvalueEditor();  //works
//        valueEditor = $f.get(0).contentWindow.getvalueEditor(); //works    
//        alert(valueEditor);
    });

});
$(window).load(function () {

});

function addProject(project) {
    //render file project

    var filesText = "";

    var foldersText = "";


    for (var p = 0; p < project.folders.length; p++) {

        filesText += addFolder(project.folders[p]);
//        alert("folder name " + project.folders[p].name);
    }



    for (var i = 0; i < project.files.length; i++) {
        filesText += addFile(project.files[i]);
    }





    $("#projectBar").append(filesText);



    $(document).ready(function () {
        $(".fileNode").dblclick(function (event) {

            addTabFile(event.target.id)


        });
    });


}

var tabFileName=[];
var tabFileCondition=[];

function addTabFile(nameFile) {

    var frameIdSource = 'frame_' + nameFile.replace(".", "").replace("/", "");
    var codeSourceId = nameFile.replace(".", "AAA").replace("/", "AAA");
//
//    $("#tabCode").append('<li id="make"><a href="#frame_' + nameFile.replace(".", "").replace("/", "") + '">' + nameFile + '</a><span style="margin-top:  -6px;margin-left: -17px; color: #eaeaea" class="on-left mif-cancel fg-hover-yellow "></span></li>');
//    var sourcePane = '<div class="frame" id="frame_' + nameFile.replace(".", "").replace("/", "") + '" style="margin-top: 0px;padding: 0px">' +
//            '<iframe  frameborder="1" width="100%" height="555" src="editor/editor_1.html"></iframe>' +
//            '</div>';



    $("#tabCode").append('<li class="tabPanel" id="' + nameFile + '" ><a id="' + nameFile + '" href="#' + frameIdSource + '">' + nameFile + '</a><span id="' + nameFile + '" style="margin-top:  -6px;margin-left: -17px; color: #eaeaea" class="on-left mif-cancel fg-hover-yellow "></span></li>');
    var sourcePane = '<div class="frame" id="' + frameIdSource + '" style="margin-top: 0px;padding: 0px">' +
            '<iframe id="' + codeSourceId + '"  name="mmm"  frameborder="1" width="100%" height="555" src="editor/editor2.html"></iframe>' +
            '</div>';

    $("#tabSource").append(sourcePane);




    $(document).ready(function () {
        $(".tabPanel").click(function (event) {
//            alert("kena sini "+event.target.id);
            requestTohh(codeSourceId);

        });
    });


//    setTimeout(requestTohh(codeSourceId), 1000);

//        alert($("#"+frameIdSource))


//    var makeUI = $.get("http://localhost:8080/project/get/file/" + nameFile, function (data) {
////        alert(frameIdSource + "data");
//        var $f = $('#mmm');
//        var valueEditor = $f[0].contentWindow.getvalueEditor();  //works
//        valueEditor = $f.get(0).contentWindow.getvalueEditor(); //works    
//        alert(valueEditor);
////        var tt="#frameCode"
////        var $f = $('#frame_' + nameFile.replace(".", "").replace("/", "") );
//
////        addProject(data.project_structure);
//    }).done(function () {
////        alert("second success ");
//    }).fail(function (data) {
////        console.log(data);
////        alert("error "+data);
//
//    }).always(function () {
////        alert("finished");
//    });


//    requestTohh();

}

function requestTohh(name) {
//    alert("ha");


//    var $f = $('#'+name);
//    var $f = $('#frameCode');
//    var valueEditor = $f[0].contentWindow.getvalueEditor();  //works
//    valueEditor = $f.get(0).contentWindow.getvalueEditor(); //works    
//    alert(valueEditor);
//    valueEditor = $f.get(0).contentWindow.setValueEditor("makan malam"); //works    
//    alert("ha2");
//    




    var makeUI = $.get("http://localhost:8080/project/get/file/"+name, function (data) {
//        alert(frameIdSource + "data");
//        alert("haasd");
        var $f = $('#'+name);
        valueEditor = $f.get(0).contentWindow.setValueEditor(data); //works    
//        alert("ha2");
//        var valueEditor = $f[0].contentWindow.getvalueEditor();  //works
//        valueEditor = $f.get(0).contentWindow.getvalueEditor(); //works    
//        alert(valueEditor);
//        var tt="#frameCode"
//        var $f = $('#frame_' + nameFile.replace(".", "").replace("/", "") );

//        addProject(data.project_structure);
    }).done(function () {
        alert("second success ");
    }).fail(function (data) {
//        console.log(data);
        alert("error " + data);

    }).always(function () {
        alert("finished");
    });
}


function addFolder(folder) {
    //find folder
    var filesText = "";
    for (var p = 0; p < folder.files.length; p++) {
        filesText += addSubFile(folder.files[p], folder.name);
    }
    return '<li class="node" ><span class="leaf"><span class="mif-folder"></span> ' + folder.name + '</span><span class="node-toggle"></span><ul>' + filesText + '</ul></li>';
}


function addSubFile(file, folderName) {
    return '<li class="fileNode" id="' + folderName + '/' + file.name + '" ><span id="' + folderName + '/' + file.name + '" class="leaf"><span id="' + folderName + '/' + file.name + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
}

function addFile(file) {
    return '<li class="fileNode" id="' + file.name + '" ><span id="' + file.name + '" class="leaf"><span id="' + file.name + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
}


function specialClick() {
    var charm = $("#menu-special").data("charm");
    if (charm.element.data("opened") === true) {
        charm.close();
    } else {
        charm.open();
    }
}

function getFileSourceFromEditor() {




}

