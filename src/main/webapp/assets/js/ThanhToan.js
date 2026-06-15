
    const form = document.getElementById('thanhtoanForm');

    form.addEventListener('submit', function(event) {

        event.preventDefault();
        if (form.checkValidity()) {
            alert('🎉 Đã đặt hàng thành công!');
        } else {
            form.reportValidity();
        }
    });
    function applyVoucher() {
        const codeInput = document.getElementById("voucherCode");
        const responseDiv = document.getElementById("voucherResponse");

        if (!codeInput) return;
        const code = codeInput.value.trim();

        if (code === "") {
            responseDiv.style.display = "block";
            responseDiv.style.color = "#d0011b";
            responseDiv.innerHTML = "Vui lòng nhập mã voucher!";
            return;
        }

        // Sử dụng URLSearchParams để giả lập dữ liệu gửi lên từ Form (x-www-form-urlencoded)
        const params = new URLSearchParams();
        params.append('action', 'apply_voucher');
        params.append('voucherCode', code);

        // Gửi POST request trực tiếp tới CheckoutServlet
        fetch('${pageContext.request.contextPath}/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        })
            .then(response => {
                // Vì Servlet sau khi xử lý voucher thành công sẽ thực hiện gọi lại doGet()
                // để forward ra giao diện, nên fetch() nhận được toàn bộ mã nguồn HTML mới.
                // Ta chỉ việc tải lại trang để hiển thị thông tin tiền mới cập nhật trong session.
                window.location.reload();
            })
            .catch(error => {
                console.error("Lỗi áp dụng voucher:", error);
                responseDiv.style.display = "block";
                responseDiv.style.color = "#d0011b";
                responseDiv.innerHTML = "Có lỗi xảy ra, vui lòng thử lại sau.";
            });
    }
