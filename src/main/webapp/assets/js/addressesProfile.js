function openEditModal(id, name, phone, address) {
  document.getElementById('editAddressModal').style.display = 'block';
  document.getElementById('edit_id').value = id;
  document.getElementById('edit_name').value = name;
  document.getElementById('edit_phone').value = phone;
  document.getElementById('edit_address').value = address;
}

const host = "https://provinces.open-api.vn/api/";

var callAPI = (api) => {
  return fetch(api)
      .then((response) => response.json())
      .then((data) => {
        data.sort((a, b) => a.name.localeCompare(b.name));
        let row = '<option value="">Chọn Tỉnh/Thành phố</option>';
        data.forEach(element => {
          row += `<option value="${element.code}">${element.name}</option>`;
        });
        document.querySelector("#province").innerHTML = row;
      });
}

var callApiDistrict = (api) => {
  return fetch(api)
      .then((response) => response.json())
      .then((data) => {
        let districts = data.districts;
        districts.sort((a, b) => a.name.localeCompare(b.name));
        let row = '<option value="">Chọn Quận/Huyện</option>';
        districts.forEach(element => {
          row += `<option value="${element.code}">${element.name}</option>`;
        });
        document.querySelector("#district").innerHTML = row;
        document.querySelector("#ward").innerHTML = '<option value="">Chọn Phường/Xã</option>';
      });
}

var callApiWard = (api) => {
  return fetch(api)
      .then((response) => response.json())
      .then((data) => {
        let wards = data.wards;
        wards.sort((a, b) => a.name.localeCompare(b.name));
        let row = '<option value="">Chọn Phường/Xã</option>';
        wards.forEach(element => {
          row += `<option value="${element.code}">${element.name}</option>`;
        });
        document.querySelector("#ward").innerHTML = row;
      });
}

callAPI(host + "?depth=1");

  document.querySelector("#province").addEventListener("change", function() {
  let code = this.value;
  if(code) {
  callApiDistrict(host + "p/" + code + "?depth=2");
} else {
  document.querySelector("#district").innerHTML = '<option value="">Chọn Quận/Huyện</option>';
  document.querySelector("#ward").innerHTML = '<option value="">Chọn Phường/Xã</option>';
}
});

  document.querySelector("#district").addEventListener("change", function() {
  let code = this.value;
  if(code) {
  callApiWard(host + "d/" + code + "?depth=2");
} else {
  document.querySelector("#ward").innerHTML = '<option value="">Chọn Phường/Xã</option>';
}
});

  document.querySelector("#addAddressForm form").addEventListener("submit", function(e) {
  const pSelect = document.querySelector("#province");
  const dSelect = document.querySelector("#district");
  const wSelect = document.querySelector("#ward");

  document.querySelector("#provinceName").value = pSelect.options[pSelect.selectedIndex].text;
  document.querySelector("#districtName").value = dSelect.options[dSelect.selectedIndex].text;
  document.querySelector("#wardName").value = wSelect.options[wSelect.selectedIndex].text;
});
  document.querySelector("input[name='new_phone']").addEventListener("input", function (e) {
  this.value = this.value.replace(/[^0-9]/g, '');

  if (this.value.length > 10) {
  this.value = this.value.slice(0, 10);
}
});