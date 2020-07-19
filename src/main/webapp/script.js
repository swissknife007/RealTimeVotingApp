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
/* Editable marker that displays when a user clicks in the map. */
let editMarker;

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
    var optionCell = document.getElementById("optionCell");
    var input = document.createElement("input");
    input.type = "text";
    input.id = "Options";
    input.name = "option";
    input.classList = "form-control";
    input.required = true;
    var br = document.createElement("br");
    br.id = "added";
    optionCell.appendChild(input);
    optionCell.appendChild(br);
  };
  var btn = document.getElementById("btn");
  btn.onclick = function () {
    document.getElementById("Options").remove();
    document.getElementById("added").remove();
  };

  var map_btn = document.getElementById("map_btn");
  map_btn.onclick = function () {
    if (document.getElementById('map') == null) {
      var container = document.getElementById("container");
      var div_map = document.createElement("div");
      div_map.id = "map";
      container.appendChild(div_map);

      map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: USA_lat, lng: USA_lng}, zoom: 4});

      map.addListener('click', (event) => {
        createEditMarker(event.latLng.lat(), event.latLng.lng());
      });
      //fetchMarkers();
    }
  };

  var map_del_btn = document.getElementById("map_del_btn");
  map_del_btn.onclick = function () {
    document.getElementById("map").remove();
  };
};

function getInput() {
  fetch("/data")
    .then((response) => response.json())
    .then((stats) => {
      console.log(stats);
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
    postMarker(lat, lng, textBox.value);
    createDisplayMarker(lat, lng, textBox.value);
    editMarker.setMap(null);
  };

  const containerDiv = document.createElement('div');
  containerDiv.appendChild(textBox);
  containerDiv.appendChild(document.createElement('br'));
  containerDiv.appendChild(button);

  return containerDiv;
}

/** Sends a marker to the backend for saving. */
function postMarker(lat, lng, content) {
  const params = new URLSearchParams();
  params.append('lat', lat);
  params.append('lng', lng);
  params.append('content', content);

  fetch('/markers', {method: 'POST', body: params});
}

/** Fetches markers from the backend and adds them to the map. */
function fetchMarkers() {
  fetch('/markers').then(response => response.json()).then((markers) => {
    markers.forEach(
        (marker) => {
            createDisplayMarker(marker.lat, marker.lng, marker.content)
        });
  });
}

/** Creates a marker that shows a read-only info window when clicked. */
function createDisplayMarker(lat, lng, content) {
  const marker =
      new google.maps.Marker({position: {lat: lat, lng: lng}, map: map});

  const infoWindow = new google.maps.InfoWindow({content: content});
  marker.addListener('click', () => {
    infoWindow.open(map, marker);
  });
}
