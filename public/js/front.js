function cleanErrors() {
    $("#errorText").html("");
    $("#error").addClass('invisible');
}

function cleanForm() {
    $("#longUrl").val("");
}

function cleanResults(){
    $("#resultLink").html("").attr("href","");
    $("#result").addClass('invisible');
}

function showError(errorMessage) {
    $("#errorText").html(errorMessage);
    $("#error").removeClass('invisible');
}

function sendLink(long_url) {
    var storeLinkRoute = "/api/store";

    var longUrl = long_url.trim();
    var json = {
        longUrl : longUrl
    };
    doPost(storeLinkRoute, json, onSuccessStoreLink, onFailStoreLink);
}

function onAlertClose() {
    $("#errorText").html("");
    $("#error").addClass('invisible');
}

function onSuccessStoreLink(data, textStatus, jqXHR) {
    cleanErrors();
    cleanForm();
    if(jqXHR.status == 201) {
        if(data.shortUrl == undefined || data.shortUrl.trim().length == 0) {
            showError("Internal error. Got malformed reply from server");
            return;
        }
        $("#resultLink").html(data.shortUrl).attr("href", data.shortUrl);
        $("#result").removeClass('invisible');
    }
}

function onFailStoreLink(jqXHR, textStatus, errorThrown) {
    if (jqXHR != null || jqXHR != undefined) {

        var replyRaw = jqXHR.responseText;
        console.log("Reply JSON: " + replyRaw);
        var reply = JSON.parse(replyRaw);

        var errorText = reply.message;
        if (reply.errors.length > 0) {
            errorText += " <BR> Errors: <BR>";
            $.each(reply.errors, function (index, value) {
                if (value.error != "Validation failed") {
                    errorText += value.error + "<BR>";
                }
            });
        }
        showError(errorText);
    }
}

function handleForm(e) {
    e.preventDefault();
    cleanErrors();
    cleanResults();

    var isFormValid = true;

    var longUrl = $("#longUrl").val();
    console.log("Got long URL: " + longUrl);
    cleanForm();
    if (longUrl == undefined || longUrl.trim().length == 0) {
        var errorMessage = "Long URL cannot be empty";
        showError(errorMessage);
        isFormValid = false;
    }
    if (isFormValid) {
        sendLink(longUrl);
    }
}

$(document).ready(function () {
    $("#shortenIt").on('click', handleForm);
    $("#errorClose").on('click', onAlertClose)
});
