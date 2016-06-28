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
        $("#list").append(elem);
        $("#list").append($("<form action='/update' method='put' style='display:inline;'>"));
        $("#list").append($("<input type='checkbox' name='keep'/>"));
        $("#list").append($("<input type='hidden' name='id' value='" + data[i].id +"'/>"));
        $("#list").append($("<button type='submit'>Update</button>"));
        $("#list").append($("</form>"));
        $("#list").append($("<form action='/delete' method='delete' style='display:inline;'>"));
        $("#list").append($("<input type='hidden' name='id' value='" + data[i].id +"'/>"));
        $("#list").append($("<input type='text' name='delete' placeholder='Delete PW'/>"));
        $("#list").append($("<button type='submit'>Delete</button>"));
        $("#list").append($("</form>"));
        $("#list").append($("<br>"));
    }
}

$.get("/files", getFiles);
