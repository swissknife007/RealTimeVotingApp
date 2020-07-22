
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(getVotes);

/** Creates a chart and adds it to the page. */
function getVotes() {
var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
  var route = "/show?id=" + id;
  fetch(route)
      .then((response) => response.json())
      .then((stats) => {
        console.log(stats);
        if (stats == null)
        {
            document.getElementById('question').innerHTML = "ERROR! INVALID ID";
        }
        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Options');
        data.addColumn('number', 'Count');

        var question = document.getElementById("question");
        question.innerHTML= stats.question;
        question.style.textAlign = "center";

        //Iterates options retrieved from JSON
        const div = document.createElement('div');
        div.className = 'row';
        
        var x ="";
        x+= "<h2> Results: </h2>";
        x+= "<ul>";
        var j = 1;
        for (i in stats.options)
        {
            if (stats.questionType == "questionPicture")
            {
                x+= "<li>Option " + j  + "<img src='" + stats.options[i].OptionName + "'>" + "has " + stats.options[i].NumberOfVotes + " votes </li>";
                data.addRow(['Option' + j , stats.options[i].NumberOfVotes]);
            }
            else
            {
                x+= "<li>" + stats.options[i].OptionName + " has " + stats.options[i].NumberOfVotes + " votes </li>";
                data.addRow([stats.options[i].OptionName,stats.options[i].NumberOfVotes]);
            }
            j++;
        }
        x+= "</ul>";
        
        console.log(x);
        document.getElementById("options").innerHTML = x;

        const options = {
            'width':500,
            'height':400, 
            backgroundColor: "#EFEEEE"
        };

        const chart = new google.visualization.PieChart(
            document.getElementById('chartContainer'));
        chart.draw(data, options);
            });

}