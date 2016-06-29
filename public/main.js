function getFiles(data)
{
    for(var i in data)
    {
        var elem = $("<a>");
        elem.attr("href", "files/" + data[i].realFilename);
        if (data[i].nickname == null || data[i].nickname == "")
        {
            elem.text(data[i].originalFilename);
        }
        else
        {
            elem.text(data[i].nickname);
        }


        var delPW = $("<input>");
        delPW.attr("type", "text");
        delPW.attr("name", "password");
        delPW.attr("placeholder", "Delete PW");
        delPW.attr("style", "display:inline");

        var delBtn = $("<button>");
        delBtn.attr("type", "submit");
        delBtn.attr("style", "display:inline");
        delBtn.text("Delete");

        var hid = $("<input>");
        hid.attr("type", "hidden");
        hid.attr("name", "id");
        hid.attr("value", data[i].id);

        var form = $("<form>");
        form.attr("action", "/delete");
        form.attr("method", "post");
        form.attr("style", "display:inline");
        form.append(hid);
        form.append(delPW);
        form.append(delBtn);



        $("#list").append(elem);
        $("#list").append(form);
        $("#list").append("</form>");
        $("#list").append($("<br>"));
        $("#list").append($("<br>"));
    }
}

$.get("/files", getFiles);
