/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//var targetServer = "31.220.54.10";
//var targetServer = "localhost";
var targetServer = "192.168.1.184";
var boardType = "";
var processorType = "";

var project;
var tabFileArrayID = [];
var tabActive = "";
var countingTabArrayId = 0;

var countingTabSaved = 0;
var tabActiveId = "";

function openProjectById(idProject) {
    var jqxhr = $.get("http://" + targetServer + ":8080/project/openProject/" + idProject, function (data) {

        project = data;
        $("#projectName").text("~" + project.name);
        $("#arduinoType").text(project.config.arduinoType);
        $("#arduinoIC").text(project.config.icType);
        updateProjectStructure(project.sourceCode);
    }).done(function () {
        //alert("second success ");
    }).fail(function (detail) {
        //alert("error " + detail);
    }).always(function () {
        //alert("finished");
    });
}

function openProject() {
    var jqxhr = $.get("http://" + targetServer + ":8080/project/openProject/galih1994_679cfedbe131466b90c06566ee548e07", function (data) {

        project = data;
        $("#projectName").text("~" + project.name);
        $("#arduinoType").text(project.config.arduinoType);
        $("#arduinoIC").text(project.config.icType);
        updateProjectStructure(project.sourceCode);
    }).done(function () {
        //alert("second success ");
    }).fail(function (detail) {
        //alert("error " + detail);
    }).always(function () {
        //alert("finished");
    });
}

function createFile() {
    var dialog = $("#createNewFile_dialog").data('dialog');
    dialog.close();
    $("#dialog9").data('dialog').open();
    var dirPath = $("#createNewFile_inputDirectory").val();
    var fileName = $("#createNewFile_inputFileName").val();

    //alert(dirPath + " " + fileName);
    //alert("http://localhost:8080/project/createFile/" + project.id + "/" + dirPath + "/" + fileName);

    var jqxhr = $.get("http://" + targetServer + ":8080/project/createFile/" + project.id + "/" + dirPath + "/" + fileName, function (data) {

        window.setTimeout(function () {
            // Do something with response
            $("#dialog9").data('dialog').close();
            //alert(data.result);
            reloadProject(project.id);
        }, 500);

    }).done(function () {
        //alert("second success ");
    }).fail(function (detail) {
        //alert("error " + detail);
    }).always(function () {
        //alert("finished");
    });
}



function createFolder() {
    var dialog = $("#createNewFolder_dialog").data('dialog');
    dialog.close();
    $("#dialog9").data('dialog').open();
    var dirPath = $("#createNewFolder_inputDirectory").val();
    var fileName = $("#createNewFolder_inputFolderName").val();

    //alert(dirPath + " " + fileName);
    //alert("http://localhost:8080/project/createFile/" + project.id + "/" + dirPath + "/" + fileName);

    var jqxhr = $.get("http://" + targetServer + ":8080/project/createFolder/" + project.id + "/" + dirPath + "/" + fileName, function (data) {

        window.setTimeout(function () {
            // Do something with response
            $("#dialog9").data('dialog').close();
            //alert(data.result);
            reloadProject(project.id);
        }, 500);

    }).done(function () {
        //alert("second success ");
    }).fail(function (detail) {
        //alert("error " + detail);
    }).always(function () {
        //alert("finished");
    });
}

$(document).ready(function () {

    openProject();

    $("#saveProject").click(function () {
        // Send the data using post

        if (countingTabSaved < tabFileArrayID.length) {

            $("#completedSaveCounting").text('Completed ' + countingTabSaved + ' of ' + tabFileArrayID.length);
//        var cleanBase32=tabFileArrayID[countingTabSaved].replace("0","=");
            var cleanBase32 = tabFileArrayID[countingTabSaved].replace(new RegExp("0", 'g'), "=");
            //alert(cleanBase32);
            var decodeId = Base32Decode(cleanBase32);
            $("#progressSaveFileName").text('~ ' + decodeId);
            $("#saveProjectProgress").data('dialog').open();
            //alert("saving " + tabFileArrayID[countingTabSaved]);
            var $f = $('#codeSource-' + tabFileArrayID[countingTabSaved]);
            var data = $f.get(0).contentWindow.getValueEditor();
            //alert(data + " data source");
            $.ajax({
                url: 'http://' + targetServer + ':8080/project/saveFile/' + project.id + "/" + tabFileArrayID[countingTabSaved],
                type: "POST",
                processData: false,
                data: data,
                contentType: "application/json",
                success: function (message) {
                    window.setTimeout(function () {
                        // Do something with response
                        //alert(message.result + " respond save");
                        countingTabSaved++;
                        $("#saveProject").click();
                        var pb = $("#pb1").data('progress');
                        pb.set((countingTabSaved / tabFileArrayID.length) * 100);
                    }, 500);

                }
            });
        } else {
            countingTabSaved = 0;
            $("#saveProjectProgress").data('dialog').close();
            var pb = $("#pb1").data('progress');
            pb.set(0);
        }
    });




    $("#consoleButton").click(function () {
        consoleButton();
    });


    $("#uploadButton").click(function () {
        uploadHex();
    });

    $("#cleanButton").click(function () {
        cleanProject();
    });


    $("#portSerial_menuButton").click(function () {
        choosePortSerial();
    });


    $("#portSerial_buttonCancel").click(function () {
        var dialog = $("#portSerial_dialog").data('dialog');
        dialog.close();
    });

    $("#portSerial_buttonSave").click(function () {
        saveSerialPort();
    });

    $("#createNewFile_buttonCreate").click(function () {
        createFile();
    });

    $("#createNewFolder_buttonCreate").click(function () {
        createFolder();
    });

    $("#closeTab").click(function () {
        //alert("close tab");
    });

    $("#configBoardButton").click(function () {
        var dialog = $("#arduinoBoardType").data('dialog');
        dialog.open();
    });

    $("#arduinoBoardType_buttonCancel").click(function () {
        var dialog = $("#arduinoBoardType").data('dialog');
        dialog.close();
    });

    $("#arduinoBoardType_buttonSave").click(function () {
        boardType = $("#arduinoBoardType_selectedBoard").val();
        processorType = $("#arduinoBoardType_selectedProcessor").val();

        var dialog = $("#arduinoBoardType").data('dialog');
        dialog.close();
        $("#arduinoBoardType_name").text(boardType);
        alert(boardType + " " + processorType);





        var makeUI = $.get("http://" + targetServer + ":8080/project/updateBoardType/" + project.id + "/" + boardType + "/" + processorType, function (data) {


            alert(data.result);


        }).done(function () {

        }).fail(function (data) {

        }).always(function () {

        });



    });




    $("#createNewFolder_fluent").click(function () {
        var dialog = $("#createNewFolder_dialog").data('dialog');
        dialog.open();
    });

    $("#createNewFolder_buttonCancel").click(function () {
        var dialog = $("#createNewFolder_dialog").data('dialog');
        dialog.close();
    });




    $("#createNewFile_fluent").click(function () {
        var dialog = $("#createNewFile_dialog").data('dialog');
        $("#createNewFile_inputDirectory").empty();
        $("#createNewFile_inputDirectory").append('<option value="%2f">/</option>');
        for (var y = 0; y < project.sourceCode.folders.length; y++) {
            var nameDir = project.sourceCode.folders[y].name;
            $("#createNewFile_inputDirectory").append('<option value="%2f' + nameDir + '">/' + nameDir + '</option>');
        }
        dialog.open();
    });

    $("#createProject_create").click(function () {

        $("#createProject_waiting").data('dialog').open();
        var projectName = $("#namaProject_create").val();
        var boardType = $("#boardType_create").val();
        var icType = $("#icType_create").val();
        var visibility = $("#visibility_create").val();
        var detail = $("#detail_create").val();
        //alert(boardType);
        var data = {
            name: projectName + "",
            board: boardType,
            ic: icType,
            detail: detail,
            visibility: visibility
        }


        $.ajax({
            url: 'http://' + targetServer + ':8080/project/create/',
            type: "POST",
            processData: false,
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (respond) {
                // Do something with response
//                //alert(message.makan);
                window.setTimeout(function () {
                    // Do something with response
                    //alert(respond.projectId);
                    $("#createProject_waiting").data('dialog').close();
                    var charm = $("#menu-special").data("charm");
                    charm.close();
                    openProjectById(respond.projectId);
                }, 500);

            }
        });
    });
    $("#backButtonPanel").click(function () {
        var charm = $("#menu-special").data("charm");
        charm.close();
    });

    $("#compileResultCloseButton").click(function () {
        var charm = $("#consoleCompiling").data("charm");
        charm.close();
    });

    $("#newProjectButton").click(function () {

        $(".sideBarLi").removeClass("active");
        $("#newProjectButton").addClass("active");
        $(".sidePanelMenuFile").hide();
        $("#newProjectPanel").show();
    });
    $("#openProjectButton").click(function () {

        $("#waitingOpenProject").show();
        $(".sideBarLi").removeClass("active");
        $("#openProjectButton").addClass("active");
        getListAllProject();
        $(".sidePanelMenuFile").hide();
        $("#openProjectPanel").show();
    });
    $("#virifyButton").click(function (event) {
        compileProject();
//        alert("verivying");
//        var charm = $("#consoleCompiling").data("charm");
//        charm.open();
//        $("#compileResultTextArea").empty();
//        var makeUI = $.get("http://" + targetServer + ":8080/project/compile/" + project.id, function (data) {
////            alert(data);    
//            $("#compileResultTextArea").append(data);
//            $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 1000);
////            var textarea = document.getElementById('compileResultTextArea');
////            textarea.scrollTop = textarea.scrollHeight;
//
//
//
//        }).done(function () {
////        //alert("second success ");
//        }).fail(function (data) {
//            //alert("error " + data);
//        }).always(function () {
//            alert("allwaysS");
//        });

//        var $f = $("#frameCode");
//        $f.get(0).contentWindow.setValueEditor("makan"); //works    
    });
});
$(window).load(function () {

});
/**
 * for deadline this day only can show lib and src
 * @param {type} project
 * @returns {undefined}
 */
function updateProjectStructure(project) {
    //render file project
    $("#projectBar").empty();
    var filesText = "";
    var foldersText = "";
    //alert("makan");
    //alert(project.folders.length);
    for (var p = 0; p < project.folders.length; p++) {
        filesText += addFolder(project.folders[p]);
    }
    for (var i = 0; i < project.files.length; i++) {
        filesText += addFile(project.files[i]);
    }

    $("#projectBar").append(filesText);
    $(document).ready(function () {
        $(".fileNode").dblclick(function (event) {
            tabActive = event.target.id;
            addTabFile(event.target.id)
        });
    });
}



function addTabFile(idFIle) {


    var tmp = idFIle.split('-', 3);
    idFIle = tmp[1];
    var fileName = tmp[2];

    for (var p = 0; p < tabFileArrayID.length; p++) {
        if (tabFileArrayID[p] == idFIle) {
            $("#fileTab-" + idFIle).click();
            return;
        }
    }

    $("#dialog9").data('dialog').open();
    tabFileArrayID[tabFileArrayID.length] = idFIle;
    var tabFileId = "fileTab-" + idFIle;
    var sourceFileId = "fileSource-" + idFIle;
    var codeFileId = "codeSource-" + idFIle;
    var closeTabId = "closeTab-" + idFIle;
//    $("#tabCode").append('<li class="tabPanel" id="' + tabFileId + '" ><a id="' + tabFileId + '" href="#' + sourceFileId + '">' + fileName + '</a><span id="' + tabFileId + '" style="margin-top:  -6px;margin-left: -17px; color: #eaeaea" class="on-left mif-cancel fg-hover-yellow "></span></li>');
    $("#tabCode").append('<li class="tabPanel ' + closeTabId + '"  ><a id="' + tabFileId + '" href="#' + sourceFileId + '">' + fileName + '</a><span id="' + closeTabId + '"  style="margin-top:  -6px;margin-left: -17px; color: #eaeaea" class="on-left mif-cancel fg-hover-yellow "></span></li>');
    var sourcePane = '<div class="frame ' + ' ' + closeTabId + '" id="' + sourceFileId + '" style="margin-top: 0px;padding: 0px">' +
            '<iframe id="' + codeFileId + '"  name="mmm"  frameborder="1" width="100%" height="555" src="editor/editor2.html"></iframe>' +
            '</div>';
    $("#tabSource").append(sourcePane);



    $(document).ready(function () {
        $("#" + tabFileId).click(function (event) {
//            //alert("klikable " + codeFileId);
            requestCodeSource(codeFileId);
            tabActiveId = idFIle;
        });



//        $(".tabPanel").click(function (event) {
////            //alert("klikable " + codeFileId);
//            requestCodeSource(codeFileId);
//            tabActiveId=idFIle;
//        });


        /**
         * error for close tab
         */
        $("#" + closeTabId).click(function (event) {
            //alert(closeTabId);
            $("." + closeTabId).remove();
            for (var p = 0; p < tabFileArrayID.length; p++) {
                if (tabFileArrayID[p] == idFIle) {
                    if (idFIle == tabActiveId) {
                        if (p > 0 && p <= tabFileArrayID.length) {
                            $("#fileTab-" + tabFileArrayID[p - 1]).click();
                        } else {
                            $("#fileTab-" + tabFileArrayID[p + 1]).click();
                        }
                    }
//                    alert("close tab number "+p);
//                    alert("lenght "+tabFileArrayID.length);
//                    if (p>=tabFileArrayID.length-1){
//                        alert("masuk open > "+ tabFileArrayID[0]);    
//                        $("#fileTab-" + tabFileArrayID[0]).click();  
//                    }else{
//                        alert("masuk open =");
//                        $("#fileTab-" + tabFileArrayID[p+1]).click();    
//                    }
                    tabFileArrayID.splice(p, 1);
//                    alert("lenght after "+tabFileArrayID.length);
                    break;
                }
            }

        });

        //to give listener to click tab
        window.setTimeout(function () {
            $("#fileTab-" + idFIle).click();
        }, 500);
    });


}

function closeTab(idFile) {

}

/**
 * danger user static valu for checking
 * @param {type} codeFileId
 * @returns {undefined}
 */
function requestCodeSource(codeFileId) {

//    //alert(codeFileId+" code file ID");
    var $f = $('#' + codeFileId);
    if ($f.get(0).contentWindow.getValueEditor() != 'null') {
        return;
    }

    var cleanId = codeFileId.split("-")[1];
    var makeUI = $.get("http://" + targetServer + ":8080/project/loadFile/" + project.id + "/" + cleanId, function (data) {
        var $f = $('#' + codeFileId);
        $f.get(0).contentWindow.setValueEditor(data);
        $("#dialog9").data('dialog').close();
    }).done(function () {
//        //alert("second success ");
    }).fail(function (data) {
        //alert("error " + data);
    }).always(function () {
//        //alert("finished");
    });
}


function addFolder(folder) {
//find folder
    var filesText = "";
    for (var p = 0; p < folder.files.length; p++) {

        filesText += addSubFile(folder.files[p], folder.name);
    }
    //alert(filesText);
    return '<li class="node" ><span class="leaf"><span class="mif-folder"></span> ' + folder.name + '</span><span class="node-toggle"></span><ul>' + filesText + '</ul></li>';
}


function addSubFile(file, folderName) {
//    return '<li class="fileNode" id="fileBar-' + file.id + "-" + file.name + '" ><span id="fileBar-' + file.id + "-" + file.name + '" class="leaf"><span id="fileBar-' + file.id + "-" + file.name + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
//return '<li class="fileNode" id="fileBar-' + file.id + "-" + file.name + '"><span id="fileBar-' + file.id + "-" + file.name + '" class="leaf"><span id="fileBar-' + file.id + "-" + file.name + '" class="icon mif-file-code">' + file.name + '</span></span></li>';
//return '<li class="fileNode" id="fileBar-' + file.id + "-" + file.name + '" ><span id="fileBar-' + file.id + "-" + file.name + '" class="icon mif-file-code leaf">' + file.name + '</span></li>';
    return '<li class="fileNode" id="fileBar-' + file.id + "-" + file.name + '" ><span id="fileBar-' + file.id + "-" + file.name + '" class="icon mif-file-code leaf" ><span  id="fileBar-' + file.id + "-" + file.name + '" style="padding-left:3px">' + file.name + '</span></span></li>';
}

function addFile(file) {
//    return '<li class="fileNode" id="fileBar-' + file.id + "-" + file.name + '" ><span id="fileBar-' + file.id + "-" + file.name + '" class="leaf"><span id="fileBar-' + file.id + "-" + file.name + '" class="icon mif-file-code"></span>' + file.name + '</span></li>';
    return '<li class="fileNode" id="fileBar-' + file.id + "-" + file.name + '" ><span id="fileBar-' + file.id + "-" + file.name + '" class="leaf icon mif-file-code"><span id="fileBar-' + file.id + "-" + file.name + '" style="padding-left:3px">' + file.name + '</span></span></li>';
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

function reloadProject(idProject) {
    //alert('reload project ' + idProject);
//    fetchingProject
    $("#fetchingProject").data('dialog').open();

    var jqxhr = $.get("http://" + targetServer + ":8080/project/openProject/" + idProject, function (data) {
        //alert('respond ' + idProject);


        window.setTimeout(function () {
            $("#fetchingProject").data('dialog').close();
            project = data;
            $("#projectName").text("~" + project.name);
            $("#arduinoType").text(project.config.arduinoType);
            $("#arduinoIC").text(project.config.icType);
            updateProjectStructure(project.sourceCode);
            var charm = $("#menu-special").data("charm");
            charm.close();


        }, 500);


    }).done(function () {
        //alert("second success ");
    }).fail(function (detail) {
        //alert("error " + detail);
    }).always(function () {
        //alert("finished");
    });
}


function getListAllProject() {
    $("#panelListOfProject").empty();

    var makeUI = $.get("http://" + targetServer + ":8080/project/getListProject", function (data) {
        //alert("dat " + data.length);
        for (var p = 0; p < data.length; p++) {
            var projectName = data[p].name;
            var dateModified = data[p].date;
            var idProjectInListOpen = data[p].id + "-list";
            var id = data[p].id + "";
            //alert("tambah incere " + idProjectInListOpen);
            var list =
                    '<li style="height: 50px;" class="listProject_open" id="' + idProjectInListOpen + '" >' +
                    '<a href = "#" >' +
                    '<span class = "title fg-black" style = "padding-left: 10px; padding-top:8px" >' +
                    '<img style = "margin-right: 7px"  src = "images/arduinoProjectIcon.png" width = "25" >' + projectName + '</span>' +
                    '<span class = "title fg-dark text-light" style = "margin-left: 42px;margin-top:-17px; font-size: 11px"> ' + dateModified + '</span>' +
                    '</a>' +
                    '</li>';
            $("#panelListOfProject").append(list);

            $("#" + idProjectInListOpen).dblclick(function (event) {
                reloadProject(this.id.split('-')[0]);
            });
        }

        window.setTimeout(function () {
            $("#panelListOfProject").show();
            $("#waitingOpenProject").hide();
        }, 500);


    }).done(function () {
//        //alert("second success ");
    }).fail(function (data) {
//        //alert("error " + data);
    }).always(function () {
//        //alert("finished");
    });




}

function Base32Decode(s) {

    return s;
}

function choosePortSerial() {


    $("#fetchingProject").data('dialog').open();


    $("#listSerialPort").empty();

    var makeUI = $.get("http://" + targetServer + ":8080/project/getListPort", function (data) {


        for (var p = 0; p < data.length; p++) {
            $("#listSerialPort").append('<option value="%2fdev%2f' + data[p] + '">/dev/' + data[p] + '</option>');
        }


        window.setTimeout(function () {
            var dialog = $("#portSerial_dialog").data('dialog');
            dialog.open();
            $("#fetchingProject").data('dialog').close();
        }, 500);


    }).done(function () {
//        alert("done");
    }).fail(function (data) {
//        alert("failed");
    }).always(function () {
//        alert("allways");
    });




}

function compileProject() {
    var charm = $("#consoleCompiling").data("charm");
    charm.open();
    $("#compileResultTextArea").empty();
    if (window.WebSocket) {
        socket = new WebSocket("ws://" + targetServer + ":8080/project/compile/" + project.id);
        socket.onmessage = function (event) {
            $("#compileResultTextArea").append(event.data);
            $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 0);
        }
        socket.onopen = function (event) {

        };
        socket.onclose = function (event) {

        };
    } else {
        alert("Your browser does not support Websockets. (Use Chrome)");
    }
}




function cleanProject() {

//    var charm = $("#consoleCompiling").data("charm");
//    charm.open();
//    $("#compileResultTextArea").empty();
//    var makeUI = $.get("http://" + targetServer + ":8080/project/clean/" + project.id, function (data) {
//
//        $("#compileResultTextArea").append(data);
//        $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 1000);
//
//    }).done(function () {
////        //alert("second success ");
//    }).fail(function (data) {
//        //alert("error " + data);
//    }).always(function () {
////        alert("allwaysS");
//    });


    var charm = $("#consoleCompiling").data("charm");
    charm.open();
    $("#compileResultTextArea").empty();
    if (window.WebSocket) {
        socket = new WebSocket("ws://" + targetServer + ":8080/project/clean/" + project.id);
        socket.onmessage = function (event) {
            $("#compileResultTextArea").append(event.data);
            $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 0);
        }
        socket.onopen = function (event) {

        };
        socket.onclose = function (event) {

        };
    } else {
        alert("Your browser does not support Websockets. (Use Chrome)");
    }


}

function uploadHex() {
//    var charm = $("#consoleCompiling").data("charm");
//    charm.open();
//    $("#compileResultTextArea").empty();
//    var makeUI = $.get("http://" + targetServer + ":8080/project/upload/" + project.id, function (data) {
////            alert(data);    
//        $("#compileResultTextArea").append(data);
//        $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 1000);
////            var textarea = document.getElementById('compileResultTextArea');
////            textarea.scrollTop = textarea.scrollHeight;
//    }).done(function () {
////        //alert("second success ");
//    }).fail(function (data) {
//        //alert("error " + data);
//    }).always(function () {
////        alert("allwaysS");
//    });


//    var charm = $("#consoleCompiling").data("charm");
//    charm.open();
//    $("#compileResultTextArea").empty();
//    $.ajax({
//        url: "http://" + targetServer + ":8080/project/upload/" + project.id,
//        data: {
//            format: 'json'
//        },
//        error: function () {
//            alert("some eroorr");
//        },
//        dataType: 'jsonp',
//        success: function (data) {
//        $("#compileResultTextArea").append(data);
//        $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 1000);
//        },
//        type: 'GET'
//    });


    var charm = $("#consoleCompiling").data("charm");
    charm.open();
    $("#compileResultTextArea").empty();


    if (window.WebSocket) {
        socket = new WebSocket("ws://" + targetServer + ":8080/project/upload/" + project.id);
        socket.onmessage = function (event) {
            $("#compileResultTextArea").append(event.data);
            $("#compileResultTextArea").animate({scrollTop: $('#compileResultTextArea').prop("scrollHeight")}, 0);
        }
        socket.onopen = function (event) {

        };
        socket.onclose = function (event) {

        };
    } else {
        alert("Your browser does not support Websockets. (Use Chrome)");
    }
}


function saveSerialPort() {
    var selectedSerialPort = $("#listSerialPort").val();
    alert(selectedSerialPort);
    $("#portSerial_labelName").text(selectedSerialPort.replace(/%2f/g, "/"));
    var dialog = $("#portSerial_dialog").data('dialog');
    dialog.close();
    alert(project.id);

    var makeUI = $.get("http://" + targetServer + ":8080/project/updateSerialPort/" + project.id + "/" + selectedSerialPort, function (data) {


        alert(data.result);
//        window.setTimeout(function () {
//            var dialog = $("#portSerial_dialog").data('dialog');
//            dialog.open();
//            $("#fetchingProject").data('dialog').close();
//        }, 500);


    }).done(function () {
//        alert("done");
    }).fail(function (data) {
//        alert("failed");
    }).always(function () {
//        alert("allways");
    });
}

function consoleButton() {
    alert("console click");
    var charm = $("#serialMonitor").data("charm");
    charm.open();





    if (window.WebSocket) {
        socket = new WebSocket("ws://" + targetServer + ":8080/project/monitor/" + project.id);
        socket.onmessage = function (event) {
            $("#serialResultTextArea").append(event.data);
            $("#serialResultTextArea").animate({scrollTop: $('#serialResultTextArea').prop("scrollHeight")}, 0);
        }
        socket.onopen = function (event) {
            $("#sendSerialButton").click(function () {
                var valueSerial = $("#sendSerialTextField").val();
                socket.send(valueSerial);
//                alert("serial button di click akhrinya "+valueSerial);
            });
        };
        socket.onclose = function (event) {

        };




    } else {
        alert("Your browser does not support Websockets. (Use Chrome)");
    }





}