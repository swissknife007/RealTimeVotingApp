// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

let map;
// Editable marker
let editMarker;
let markerLats = [];
let markerLngs = [];
let markerContents = [];

const USA_lat = 37.0902;
const USA_lng = -95.7129;

async function getData() {
  throw "Impliment getData";
}

function createListElement(text) {
  const liElement = document.createElement("li");
  liElement.innerText = text;
  return liElement;
}

window.onload = function () {
  document.getElementById("addOption").onclick = function () {
    let optionCell = document.getElementById("optionCell");
    var input = document.createElement("input");
    input.type = "text";
    input.id = "Options";
    input.name = "option";
    input.classList = "form-control";
    input.required = true;
    let br = document.createElement("br");
    br.id = "added";
    optionCell.appendChild(input);
    optionCell.appendChild(br);
  };
  
  document.getElementById("btn").onclick = function () {
    document.getElementById("Options").remove();
    document.getElementById("added").remove();
  };

  // Sets up map
  if(document.getElementById("questionTypeMap").hidden != null) {
    if (document.getElementById('map') == null) {
      var container = document.getElementById("questionTypeMap");
      var div_map = document.createElement("div");
      div_map.id = "map";
      container.appendChild(div_map);

      map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: USA_lat, lng: USA_lng}, zoom: 4});
      // Allows for marker placement
      map.addListener('click', (event) => {
        createEditMarker(event.latLng.lat(), event.latLng.lng());
      });
    }
  } else if (document.getElementById("questionTypeMap").hidden == null) {
    document.getElementById("map").remove();
    markerLats = [];
    markerLngs = [];
    markerContents = [];
  };

  // Calls post function from MapRoom and passes in markers from global list
  // as parameters
  document.getElementById("map-register").onclick = function () {
    const params = new URLSearchParams();
    params.append('lats', markerLats);
    params.append('lngs', markerLngs);
    params.append('contents', markerContents);
    params.append('question', document.getElementById("question").value);
    markerLats = [];
    markerLngs = [];
    markerContents = [];
    fetch('/mapRoom', {method: 'POST', body: params})

    //It retrieves the URL send from the MapRoom.java and stores as response. 
    .then(response=>response.text()).then((stats)=>
        {window.location.replace(stats);});
  }
};

function addPictures(id) {
    let vid = document.getElementById(id);
    let input = document.createElement("input");
    let optionCell = document.getElementById("optionCell2");
    input.name = "image";
    input.type = "file";
    // input.value = "Add Pictures";
    input.accept= "image/png, image/jpeg";
    let br = document.createElement("br");
    br.id = "added";
    optionCell.appendChild(input);
    optionCell.appendChild(br);
// <input name="image" class="addFile" type="file" id="addFile" value="Add Pictures" accept="image/png, image/jpeg" required />

};

function searchRoom() {
    var url_string = window.location.href;
    var url = new URL(url_string);
    const parameter = "searchBar";
    var parameterValue = url.searchParams.get(parameter);
    console.log("ParameterValue is " + parameterValue);

    var route = "/search?" + parameter + "=" + parameterValue;
    console.log("Route is " + route);
    fetch(route)
        .then((response) => response.json())
        .then((stats) => {
            console.log(stats);
            let tag = document.createElement("h2");

            // Attaches loading to variable to be deleted after query is processed
            var loading = document.getElementById("str-loading");
            var animLoading = document.getElementById("loading");
            if (stats == null)
            {
                loading.remove();
                animLoading.parentNode.removeChild(animLoading);
                let result = document.createTextNode("No matches found");
                tag.appendChild(result);
                let div = document.getElementById("searchResults");
                div.appendChild(tag);
            }
            else
            {
                let address = location.protocol + '//' + location.host;
                let absoluteURL = new URL(address);
                absoluteURL.pathname = "votePage.html";
                let queryParameter = "?id=";
                let i = 0;
                let x = "<h2> You entered: " + parameterValue + " </h2><br>";
                x += "<ul>";
                for(i in stats.roomID)
                {   
                    x+= "<li><a href='"+absoluteURL + queryParameter + stats.roomID[i].ID + "'/>" + stats.roomID[i].title +"</a></li>";
                    i++;
                }
                x+= "</ul>";

                // Remove the loading screen from search page as the query is ready to display
                loading.remove();
                animLoading.parentNode.removeChild(animLoading);                
                document.getElementById("searchResults").innerHTML = x; 
                console.log("You have " + i + " questions found");
            }    
            });
}


//Creates a marker that shows a textbox the user can edit
function createEditMarker(lat, lng) {
  // If we're already showing an editable marker, then remove it.
  if (editMarker) {
    editMarker.setMap(null);
  }

  editMarker =
    new google.maps.Marker({position: {lat: lat, lng: lng}, map: map});

  const infoWindow =
    new google.maps.InfoWindow({content: buildInfoWindow(lat, lng)});

  // When the user closes the editable info window, remove the marker.
  google.maps.event.addListener(infoWindow, 'closeclick', () => {
    editMarker.setMap(null);
  });

  infoWindow.open(map, editMarker);
}

//Creates an editable textbox and a submit button for marker
function buildInfoWindow(lat, lng) {
  const textBox = document.createElement('textarea');
  const button = document.createElement('button');
  button.appendChild(document.createTextNode('Submit Marker'));

  button.onclick = () => {
    createDisplayMarker(lat, lng, textBox.value);
    editMarker.setMap(null);
  };

  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);

  return containerDiv;
}

/** Creates a marker that shows a read-only info window when clicked. */
function createDisplayMarker(lat, lng, content) {
  const marker =
      new google.maps.Marker({position: {lat: lat, lng: lng}, map: map});

  const infoWindow = new google.maps.InfoWindow({content: content});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
  
  markerLats.push(lat);
  markerLngs.push(lng);
  markerContents.push(content);
}

var blobURL = "";
//Get the BLOB URL once
fetch('/blobstore-upload-url').then((response) => {
        return response.text();
    }).then((imageUploadUrl) => {
        blobURL = imageUploadUrl;
    });
function enableQuestion(id) {
    const textURL = "/data";
    const formId = "vote-form";

    const questionText = "questionText";
    const questionTypeText = "questionTypeText";
    const questionPicture = "questionPicture";
    const questionTypePictures = "questionTypePictures";
    const questionMap = "questionMap";
    const questionTypeMap = "questionTypeMap";
    const survey = "btn-register";
    const map = "map-register";

    var textSection = document.getElementById(questionTypeText);
    var pictureSection = document.getElementById(questionTypePictures);
    var mapSection = document.getElementById(questionTypeMap);
    var form = document.getElementById(formId);
    var enctypeValue = "multipart/form-data";
    var surveyButton = document.getElementById(survey);
    var mapButton = document.getElementById(map);
 
    // Text-type selected
    if (id == questionText) {
        if (!(textSection.hidden)) { // Uncheck
            document.getElementById(id).checked = false;
            textSection.hidden = true;
            pictureSection.hidden = true;
            mapSection.hidden = true;
            surveyButton.hidden = true;
            mapButton.hidden = true;
        } else { // Check
            form.action = textURL;
            form.removeAttribute("enctype");
            textSection.hidden = false;
            pictureSection.hidden = true;
            mapSection.hidden = true;
            surveyButton.hidden = false;
            surveyButton.value = "Click to Create a Room";
            mapButton.hidden = true;
        }
    } else if (id == questionPicture) { // Image-type selected
       if (!(pictureSection.hidden)) { // Uncheck
            document.getElementById(id).checked = false;
            textSection.hidden = true;
            pictureSection.hidden = true;
            mapSection.hidden = true;
            surveyButton.hidden = true;
            mapButton.hidden = true;
        } else { // Check
        //Change the URL request accordingly to the choice chosen accordingly
            form.action = blobURL;
            form.enctype = enctypeValue;
            pictureSection.hidden = false;
            textSection.hidden = true;
            mapSection.hidden = true;
            surveyButton.hidden = false;
            surveyButton.value = "Click to Create a Picture Room";
            mapButton.hidden = true;
        }
    } else if (id == questionMap) { // Map-type selected
        if (!(mapSection.hidden)) { // Uncheck
            document.getElementById(id).checked = false;
            textSection.hidden = true;
            pictureSection.hidden = true;
            mapSection.hidden = true;
            surveyButton.hidden = true;
            mapButton.hidden = true;
        } else { // Check
            form.action = textURL;
            form.removeAttribute("enctype");
            mapSection.hidden = false;
            textSection.hidden = true;
            pictureSection.hidden = true;
            surveyButton.hidden = true;
            mapButton.hidden = false;
        }
    }
}

// Validates input entered from user in text-form
function validateInput(){
    var inputs = document.getElementsByName("option");
    var isEqual = false;

    loop:  // Label to be used to stop double loops
    for (var i = 0; i < inputs.length; i++)
        for(var j = i+1; j < inputs.length; j++)
            if (inputs[i].value == inputs[j].value)
            {
                alert("Invalid Option!");
                event.preventDefault();
                break loop;
            }
}