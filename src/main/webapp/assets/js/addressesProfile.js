function openEditModal(id, name, phone, address) {
  document.getElementById('editAddressModal').style.display = 'block';
  document.getElementById('edit_id').value = id;
  document.getElementById('edit_name').value = name;
  document.getElementById('edit_phone').value = phone;
  document.getElementById('edit_address').value = address;
}

const host = "https://provinces.open-api.vn/api/";

var callAPI = (api) => {
    return fetch(api).then((response) => response.json()).then((data) => {
        data.sort((a, b) => a.name.localeCompare(b.name));
        let row = '<option value="">Chọn Tỉnh/Thành phố</option>';
        data.forEach(element => { row += `<option value="${element.code}">${element.name}</option>`; });
        document.querySelector("#province").innerHTML = row;
    });
}

var callApiDistrict = (api) => {
    return fetch(api).then((response) => response.json()).then((data) => {
        let districts = data.districts;
        districts.sort((a, b) => a.name.localeCompare(b.name));
        let row = '<option value="">Chọn Quận/Huyện</option>';
        districts.forEach(element => { row += `<option value="${element.code}">${element.name}</option>`; });
        document.querySelector("#district").innerHTML = row;
        document.querySelector("#ward").innerHTML = '<option value="">Chọn Phường/Xã</option>';
    });
}

var callApiWard = (api) => {
    return fetch(api).then((response) => response.json()).then((data) => {
        let wards = data.wards;
        wards.sort((a, b) => a.name.localeCompare(b.name));
        let row = '<option value="">Chọn Phường/Xã</option>';
        wards.forEach(element => { row += `<option value="${element.code}">${element.name}</option>`; });
        document.querySelector("#ward").innerHTML = row;
    });
}

callAPI(host + "?depth=1");

document.querySelector("#province").addEventListener("change", function() {
    let code = this.value;
    if(code) callApiDistrict(host + "p/" + code + "?depth=2");
    else {
        document.querySelector("#district").innerHTML = '<option value="">Chọn Quận/Huyện</option>';
        document.querySelector("#ward").innerHTML = '<option value="">Chọn Phường/Xã</option>';
    }
});

document.querySelector("#district").addEventListener("change", function() {
    let code = this.value;
    if(code) callApiWard(host + "d/" + code + "?depth=2");
    else document.querySelector("#ward").innerHTML = '<option value="">Chọn Phường/Xã</option>';
});

document.querySelector("#addAddressForm form").addEventListener("submit", function() {
    const pSelect = document.querySelector("#province");
    const dSelect = document.querySelector("#district");
    const wSelect = document.querySelector("#ward");
    document.querySelector("#provinceName").value = pSelect.options[pSelect.selectedIndex].text;
    document.querySelector("#districtName").value = dSelect.options[dSelect.selectedIndex].text;
    document.querySelector("#wardName").value = wSelect.options[wSelect.selectedIndex].text;
});

// ==========================================
// HÀM MỞ MODAL VÀ TỰ ĐỘNG CHỌN ĐỊA CHỈ (ASYNC)
// ==========================================
async function openEditModal(id, name, phone, streetDetail, wardName, districtName, provinceName) {
    document.getElementById('editAddressModal').style.display = 'block';

    document.getElementById('edit_id').value = id;
    document.getElementById('edit_name').value = name;
    document.getElementById('edit_phone').value = phone;
    document.getElementById('edit_streetDetail').value = streetDetail;

    let pSelect = document.querySelector("#edit_province");
    let dSelect = document.querySelector("#edit_district");
    let wSelect = document.querySelector("#edit_ward");

    let pRes = await fetch(host + "?depth=1");
    let provinces = await pRes.json();
    pSelect.innerHTML = '<option value="">Chọn Tỉnh/Thành phố</option>';
    let matchedProvCode = "";

    provinces.sort((a, b) => a.name.localeCompare(b.name)).forEach(p => {
        pSelect.innerHTML += `<option value="${p.code}">${p.name}</option>`;
        if(p.name === provinceName) matchedProvCode = p.code;
    });

    if (matchedProvCode) {
        pSelect.value = matchedProvCode;
        let dRes = await fetch(host + "p/" + matchedProvCode + "?depth=2");
        let dData = await dRes.json();
        dSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
        let matchedDistCode = "";

        dData.districts.sort((a, b) => a.name.localeCompare(b.name)).forEach(d => {
            dSelect.innerHTML += `<option value="${d.code}">${d.name}</option>`;
            if(d.name === districtName) matchedDistCode = d.code;
        });

        if (matchedDistCode) {
            dSelect.value = matchedDistCode;
            let wRes = await fetch(host + "d/" + matchedDistCode + "?depth=2");
            let wData = await wRes.json();
            wSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';

            wData.wards.sort((a, b) => a.name.localeCompare(b.name)).forEach(w => {
                wSelect.innerHTML += `<option value="${w.code}">${w.name}</option>`;
                if(w.name === wardName) wSelect.value = w.code;
            });
        }
    }
}

document.querySelector("#edit_province").addEventListener("change", async function() {
    let code = this.value;
    let dSelect = document.querySelector("#edit_district");
    let wSelect = document.querySelector("#edit_ward");
    dSelect.innerHTML = '<option value="">Chọn Quận/Huyện</option>';
    wSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';

    if(code) {
        let res = await fetch(host + "p/" + code + "?depth=2");
        let data = await res.json();
        data.districts.sort((a, b) => a.name.localeCompare(b.name)).forEach(d => {
            dSelect.innerHTML += `<option value="${d.code}">${d.name}</option>`;
        });
    }
});

document.querySelector("#edit_district").addEventListener("change", async function() {
    let code = this.value;
    let wSelect = document.querySelector("#edit_ward");
    wSelect.innerHTML = '<option value="">Chọn Phường/Xã</option>';

    if(code) {
        let res = await fetch(host + "d/" + code + "?depth=2");
        let data = await res.json();
        data.wards.sort((a, b) => a.name.localeCompare(b.name)).forEach(w => {
            wSelect.innerHTML += `<option value="${w.code}">${w.name}</option>`;
        });
    }
});

document.querySelector("#editAddressModal form").addEventListener("submit", function(e) {
    const pSelect = document.querySelector("#edit_province");
    const dSelect = document.querySelector("#edit_district");
    const wSelect = document.querySelector("#edit_ward");

    document.querySelector("#edit_provinceName").value = pSelect.options[pSelect.selectedIndex].text;
    document.querySelector("#edit_districtName").value = dSelect.options[dSelect.selectedIndex].text;
    document.querySelector("#edit_wardName").value = wSelect.options[wSelect.selectedIndex].text;
});

document.querySelectorAll("input[name='new_phone'], input[name='edit_phone']").forEach(input => {
    input.addEventListener("input", function() {
        this.value = this.value.replace(/[^0-9]/g, '');
        if (this.value.length > 10) this.value = this.value.slice(0, 10);
    });
});