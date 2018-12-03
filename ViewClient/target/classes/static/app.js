var stompClient = null;

var state = false;

function setConnected(value) {
    state = value;
}
function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/faculties', function (facultyResponse) {
            updateView(JSON.parse(facultyResponse.body));
            sendName();
        });
        sendName();
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
   stompClient.send("/app/faculties/hello", {}, JSON.stringify({'requestMsg': "Frontend is ready for new updates"}));
}

function updateView(response){
    //console.log(response);
    clearFacultiesTable()
     response.facultyList.forEach(function(Result) {
        $('#facultiesTable > tbody:last-child')
            .append('<tr> <td>'+Result.facultyId+'</td><td>'+Result.facultyName+'</td><td/></tr>');
    });
    $("#lastUpdate h4").remove();
    $("#lastUpdate").append('<h4>Last updated' + '<b>' + response.formattedDate +'</b>'+ '</h4>')
    console.log(response.updateTime)
}

function clearFacultiesTable(){
    $("#facultiesTable tr").slice(1).remove();
}
$(function () {

    $( "#sendButton" ).click(function() { sendName(); });
    connect();
});