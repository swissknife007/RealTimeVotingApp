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
};

function getInput() {
  fetch("/data")
    .then((response) => response.json())
    .then((stats) => {
      console.log(stats);
    });
}

/** Creates a map that allows users to add markers. */
function createMap() {
  map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 38.5949, lng: -94.8923}, zoom: 4});

  // When the user clicks in the map, show a marker with a text box the user can
  // edit.
  //map.addListener('click', (event) => {
    //createMarkerForEdit(event.latLng.lat(), event.latLng.lng());
  //});
  //fetchMarkers();
}
