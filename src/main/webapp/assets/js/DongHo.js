const filterButton = document.getElementById('filterButton'); 
const filterDropdown = document.getElementById('filterDropdown');
const pageOverlay = document.getElementById('pageOverlay');
const applyButton = document.querySelector('.apply-button'); 
const productItems = document.querySelectorAll('.link-product'); 

function priceToNumber(priceString) {
    if (!priceString) return 0;
    return parseInt(priceString.replace('₫', '').replace(/\./g, '').trim());
}


function getBrandFromItem(itemName) {
    return itemName.split(' ')[0].trim();
}


function checkPriceRange(price, selectedRadioValue) {
    const MAX_PRICE = 200000000; 
    
    switch (selectedRadioValue) {
        case 'Dưới 1 Triệu':
            return price < 1000000;
        case 'Từ 1 Triệu - 5 Triệu':
            return price >= 1000000 && price <= 5000000;
        case 'Từ 6 - 9 Triệu':
            return price >= 6000000 && price <= 9000000;
        case 'Từ 9 - 15 Triệu':
            return price >= 9000000 && price <= 15000000;
        case 'Từ 15 - 30 Triệu': 
            return price >= 15000000 && price <= 30000000;
        case 'Từ 30 - 50 Triệu':
            return price >= 30000000 && price <= 50000000;
        case 'Từ 50 - 80 Triệu':
            return price >= 50000000 && price <= 80000000;
        case 'Từ 80 - 120 Triệu':
            return price >= 80000000 && price <= 120000000;
        case 'Từ 120 - 150 Triệu':
            return price >= 120000000 && price <= 150000000;
        case '150 Triệu':
            return price >= 150000000;
        default:
            return true; 
    }
}


function toggleDropdown(){
    filterDropdown.classList.toggle('show');
    pageOverlay.classList.toggle('active');
}


function applyFilters(event) {
    event.preventDefault(); 

    
    const selectedBrands = Array.from(document.querySelectorAll('input[name="brand"]:checked'))
        .map(checkbox => checkbox.value.trim().toLowerCase());
    
    const selectedPriceElement = document.querySelector('input[name="price"]:checked');
    const selectedPriceLabel = selectedPriceElement ? selectedPriceElement.parentElement.textContent.trim() : null;

    productItems.forEach(item => {
        const itemPriceString = item.querySelector('.PriceOfPoduct').textContent;
        const itemPrice = priceToNumber(itemPriceString);
        const itemName = item.querySelector('.Item').textContent;
        const itemBrand = getBrandFromItem(itemName).toLowerCase();

        let matchesBrand = true;
        let matchesPrice = true;

        if (selectedBrands.length > 0 && !selectedBrands.includes(itemBrand)) {
            matchesBrand = false;
        }

        if (selectedPriceLabel && !checkPriceRange(itemPrice, selectedPriceLabel)) {
            matchesPrice = false;
        }

        if (matchesBrand && matchesPrice) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });

    toggleDropdown();
}



filterButton.addEventListener('click', toggleDropdown);

applyButton.addEventListener('click', applyFilters);

document.addEventListener('click', function(event){
    if (
        !filterDropdown.contains(event.target) && 
        !filterButton.contains(event.target) &&
        filterDropdown.classList.contains('show') 
    ) {
        toggleDropdown(); 
    }
});