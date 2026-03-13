<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="site-footer">
    <div class="footer-container">
        <div class="footer-row">

            <div class="footer-col about">
                <div class="footer-logo">
                    <i class="fa-solid fa-clock"></i> ${shopInfo.brandName} Watch
                </div>
                <p class="footer-desc">
                    ${shopInfo.footerDesc}
                </p>
            </div>

            <div class="footer-col links">
                <h3>Featured</h3>
                <ul>
                    <li><a href="category?type=nam">ĐỒNG HỒ NAM</a></li>
                    <li><a href="category?type=nu">ĐỒNG HỒ NỮ</a></li>
                    <li><a href="category?type=luxury">BỘ SƯU TẬP LUXURY</a></li>
                    <li><a href="#">PHỤ KIỆN ĐỒNG HỒ</a></li>
                </ul>
            </div>

            <div class="footer-col contact">
                <h3>Liên Hệ</h3>
                <div class="contact-item">
                    <span class="label">ĐỊA CHỈ</span>
                    <p>${shopInfo.address}</p>
                </div>
                <div class="contact-item">
                    <span class="label">HOTLINE</span>
                    <p>${shopInfo.hotline}</p>
                </div>
                <div class="contact-item">
                    <span class="label">EMAIL</span>
                    <p>${shopInfo.email}</p>
                </div>
            </div>

        </div>

        <div class="footer-bottom">

            <div class="copyright">
                ${shopInfo.copyright}
            </div>

        </div>
    </div>
</footer>