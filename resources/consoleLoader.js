/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var project;


var tab = function () {
    this.fileId = "";
    this.fileName = "";
    this.sourceCode = "";
}

var tabProject = function () {
    this.tabActive = 0;
    this.maximumTab = 6;
    this.tabFiles = [];

    this.addTab = function (tab) {
//        alert("masuk tab");
        this.tabFiles[this.tabFiles.lenght++] = tab;
//        this.tabFiles[0] = tab;
//        this.tabActive = this.tabFiles.lenght;

//        return ;
    }

    this.getTabActive = function () {
        return this.tabFiles[this.tabActive];
    }

    this.selectTab = function (numberTab) {
        return this.tabFiles[numberTab];
    }
}

//var budi = function () {
//    this.tabActive = 0;
//    this.maximumTab = 6;
//    this.tabFiles = [];
//    this.makan = "minum";
//    this.getMakan = function () {
//        alert("yes");
//        return this.makan;
//    }
//    this.addTab = function () {
//        alert("yes");
//        return this.makan;
//    }
//}
//
//var budiObject = new budi();
//
//function minum() {
//
//    alert(budiObject.getMakan());
//}
//minum();

var tabProjectActive = new tabProject();
//tabProjec.addTab();

$(document).ready(function () {
    console.log("document loaded");
    var jqxhr = $.get("http://localhost:8080/project/openProject/makan", function (data) {

        project = data;



        $("#projectName").text("~" + project.name);
        $("#arduinoType").text(project.config.arduinoType);
        $("#arduinoIC").text(project.config.icType);

        updateProjectStructure(project.sourceCode);



    }).done(function () {
        alert("second success ");
    }).fail(function (detail) {
        alert("error " + detail);
    }).always(function () {
        alert("finished");
    });



    $("#backButtonPanel").click(function () {
        var charm = $("#menu-special").data("charm");

        charm.close();
    });

    $("#newProjectButton").click(function () {

        $(".sideBarLi").removeClass("active");
        $("#newProjectButton").addClass("active");

        $(".sidePanelMenuFile").hide( );
        $("#newProjectPanel").show( );
    });


    $("#openProjectButton").click(function () {
        $(".sideBarLi").removeClass("active");
        $("#openProjectButton").addClass("active");

        $(".sidePanelMenuFile").hide();
        $("#openProjectPanel").show( );
    });



    $("#virifyButton").click(function (event) {
        var $f = $("#frameCode");
        $f.get(0).contentWindow.setValueEditor("makan"); //works    
    });

});
$(window).load(function () {

});

function updateProjectStructure(project) {
    //render file project

    var filesText = "";
    var foldersText = "";
    alert("makan");
    for (var p = 0; p < project.folders.length; p++) {
        filesText += addFolder(project.folders[p]);
    }
    for (var i = 0; i < project.files.length; i++) {
        filesText += addFile(project.files[i]);
    }
    $("#projectBar").append(filesText);

    $(document).ready(function () {
        $(".fileNode").dblclick(function (event) {
            alert(event.target.id);
            $("#dialog9").data('dialog').open();
            addTabFile(event.target.id)

        });
    });


}


function addTabFile(idFIle) {

    var tabFileId = 'frame_' + idFIle.replace(".", "").replace("/", "");

    $("#tabCode").append('<li class="tabPanel" id="' + idFIle + '" ><a id="' + idFIle + '" href="#frame_5_1">' + idFIle + '</a><span id="' + idFIle + '" style="margin-top:  -6px;margin-left: -17px; color: #eaeaea" class="on-left mif-cancel fg-hover-yellow "></span></li>');
    var sourcePane = '<div class="frame" id="' + tabFileId + '" style="margin-top: 0px;padding: 0px"></div>';

    $("#tabSource").append(sourcePane);




    var makeUI = $.get("http://localhost:8080/project/loadFile/namaFIle" + idFIle, function (data) {
        $("#dialog9").data('dialog').close();

        var tabThis = new tab();
        tabThis.fileName = idFIle;

        tabThis.fileId = idFIle;
//        alert("yes success");
        tabThis.sourceCode = data;

        tabProjectActive.addTab(tabThis)
        alert("yes success " + tabProjectActive.getTabActive().sourceCode);
        var $f = $('#codeEditor');
        $f.get(0).contentWindow.setValueEditor(tabProjectActive.getTabActive().sourceCode);
    }).done(function () {
        alert("second success ");
    }).fail(function (data) {
        alert("error " + data);
    }).always(function () {
        alert("finished");
    });






    $(document).ready(function () {
        $(".tabPanel").click(function (event) {
            requestTohh(tabFileId);

        });
    });


}

function requestTohh(idtab) {
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
    return '<li class="fileNode" id="' + file.id + '" ><span id="' + file.id + '" class="leaf"><span id="' + file.id + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
}

function addFile(file) {
    return '<li class="fileNode" id="' + file.id + '" ><span id="' + file.id + '" class="leaf"><span id="' + file.id + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
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

