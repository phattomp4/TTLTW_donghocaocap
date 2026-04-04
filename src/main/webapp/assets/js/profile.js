
function openOverlayModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
}
function closeOverlayModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}


if (typeof activeModalFromJava !== 'undefined' && activeModalFromJava.trim() !== '') {
    openOverlayModal(activeModalFromJava);
}

const emailRegex = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$/i;
const phoneRegex = /^(03|05|07|08|09)\d{8}$/;

const newEmailInput = document.getElementById('newEmailInput');
if (newEmailInput) {
    newEmailInput.addEventListener('input', function() {
        let val = this.value.trim();
        let errSpan = document.getElementById('emailAjaxError');
        let btn = document.getElementById('btnSubmitEmail');

        if (!val) { errSpan.innerHTML = ''; btn.disabled = true; return; }
        if (!emailRegex.test(val)) {
            errSpan.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i> Định dạng email không hợp lệ!';
            btn.disabled = true; return;
        }

        fetch(contextPath + '/checkDuplicate?type=email&value=' + encodeURIComponent(val))
            .then(res => res.text())
            .then(msg => {
                if(msg.trim() !== "") {
                    errSpan.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i> ' + msg;
                    btn.disabled = true;
                } else {
                    errSpan.innerHTML = '<span style="color: #27ae60;"><i class="fa-solid fa-circle-check"></i> Email hợp lệ và chưa được sử dụng</span>';
                    btn.disabled = false;
                }
            });
    });
}

const newPhoneInput = document.getElementById('newPhoneInput');
if (newPhoneInput) {
    newPhoneInput.addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
        let val = this.value;
        let errSpan = document.getElementById('phoneAjaxError');
        let btn = document.getElementById('btnSubmitPhone');

        if (!val) { errSpan.innerHTML = ''; btn.disabled = true; return; }
        if (!phoneRegex.test(val)) {
            errSpan.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i> SĐT phải gồm 10 chữ số (bắt đầu bằng 03, 05, 07, 08, 09)';
            btn.disabled = true; return;
        }

        fetch(contextPath + '/checkDuplicate?type=phone&value=' + encodeURIComponent(val))
            .then(res => res.text())
            .then(msg => {
                if(msg.trim() !== "") {
                    errSpan.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i> ' + msg;
                    btn.disabled = true;
                } else {
                    errSpan.innerHTML = '<span style="color: #27ae60;"><i class="fa-solid fa-circle-check"></i> Số điện thoại hợp lệ</span>';
                    btn.disabled = false;
                }
            });
    });
}

let resendTimer;
let timeLeft = 60;

function startResendTimer() {
    const btnResend = document.getElementById('btnResendOtp');
    if (!btnResend) return;

    btnResend.disabled = true;
    btnResend.style.color = '#999';
    btnResend.style.cursor = 'not-allowed';
    timeLeft = 60;

    clearInterval(resendTimer);
    resendTimer = setInterval(() => {
        timeLeft--;
        btnResend.innerText = `Gửi lại mã (${timeLeft}s)`;
        if (timeLeft <= 0) {
            clearInterval(resendTimer);
            btnResend.innerText = 'Chưa nhận được? Gửi lại mã OTP';
            btnResend.disabled = false;
            btnResend.style.color = '#f39c12';
            btnResend.style.cursor = 'pointer';
        }
    }, 1000);
}

const otpModal = document.getElementById('otpOverlayModal');
if (otpModal && otpModal.style.display !== 'none') {
    startResendTimer();
}

const btnResendOtp = document.getElementById('btnResendOtp');
if (btnResendOtp) {
    btnResendOtp.addEventListener('click', function() {
        if (timeLeft > 0) return;

        let resendMsg = document.getElementById('resendMessage');
        resendMsg.innerText = "Đang xử lý gửi lại...";
        resendMsg.style.color = "#666";
        btnResendOtp.disabled = true;

        fetch(contextPath + '/profile', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'action=resendOtp'
        })
            .then(res => res.text())
            .then(msg => {
                if (msg.startsWith("ERROR")) {
                    resendMsg.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> ' + msg.replace("ERROR:", "").trim();
                    resendMsg.style.color = "#d0011b";
                    if(!msg.includes("3 lần")) startResendTimer();
                } else {
                    resendMsg.innerHTML = '<i class="fa-solid fa-check-circle"></i> ' + msg.replace("SUCCESS:", "").trim();
                    resendMsg.style.color = "#27ae60";
                    startResendTimer();
                }
            })
            .catch(err => {
                resendMsg.innerText = "Lỗi kết nối máy chủ!";
                resendMsg.style.color = "#d0011b";
                btnResendOtp.disabled = false;
            });
    });
}