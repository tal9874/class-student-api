// Information from my rest api student

async function JSONDataStudent() {
    const response = await fetch("http://localhost:8050/api/student");
    const jsonData = await response.json();
    let tb = "";
    for (i = 0; i < jsonData.length; i++) {
        tb += `<tr><td>${jsonData[i].id}</td>
          <td>${jsonData[i].first_name}</td>
          <td>${jsonData[i].last_name}</td>
          <td>${jsonData[i].avg_grade}</td>
          <td>${jsonData[i].gender}</td>
          <td>${jsonData[i].class_id}</td></tr>`;
    }
    document.getElementById("data_student").innerHTML = tb;
}

// Information from my rest api class room

async function JSONDataClassRoom() {
    const response = await fetch("http://localhost:8050/api/classroom");
    const jsonData = await response.json();
    let tb = "";
    for (i = 0; i < jsonData.length; i++) {
        tb += `<tr><td>${jsonData[i].id}</td>
          <td>${jsonData[i].number_of_students}</td>
          <td>${jsonData[i].class_avg}</td>
          <td>${jsonData[i].type_class}</td></tr>`;
    }
    document.getElementById("data_classroom").innerHTML = tb;
}


JSONDataStudent();
JSONDataClassRoom();

