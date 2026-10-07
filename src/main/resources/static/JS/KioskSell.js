const productSelect = document.getElementById("product");
const quantityInput = document.getElementById("quantity");
const totalText = document.getElementById("total");
const saleForm = document.getElementById("saleForm");
const saleButton = saleForm.querySelector("button[type='submit']");
const saleList = document.getElementById("saleList");

const productStatus = document.getElementById("productStatus");
const saleStatus = document.getElementById("saleStatus");
const listStatus = document.getElementById("listStatus");

function formatPrice(value) {
    return Number(value).toLocaleString("da-DK", {
        style: "currency",
        currency: "DKK"
    });
}

function formatDateTime(value) {
    return new Date(value).toLocaleString("da-DK", {
        day: "numeric",
        month: "short",
        hour: "2-digit",
        minute: "2-digit"
    });
}

async function readErrorMessage(response, fallback) {
    try {
        const data = await response.json();
        return data.message || fallback;
    } catch (e) {
        return fallback;
    }
}

function updateTotal() {
    const selected = productSelect.selectedOptions[0];
    const price = Number(selected?.dataset.price || 0);
    const quantity = Number(quantityInput.value) || 0;
    totalText.textContent = "Total: " + formatPrice(price * quantity);
}

async function loadProducts() {
    productStatus.textContent = "";

    try {
        const response = await fetch("/kiosk/products");
        if (!response.ok) {
            throw new Error();
        }
        const products = await response.json();

        productSelect.innerHTML = '<option value="">Vælg vare</option>';
        products.forEach(product => {
            const option = document.createElement("option");
            option.value = product.id;
            option.dataset.price = product.price;
            option.textContent = product.product + " – " + formatPrice(product.price);
            productSelect.appendChild(option);
        });

        if (products.length === 0) {
            productStatus.textContent = "Der er ingen varer endnu. Opret en vare under Kiosk → Opret vare.";
        }
    } catch (e) {
        productStatus.textContent = "Kunne ikke hente varer.";
    }

    updateTotal();
}

function renderSales(sales) {
    saleList.innerHTML = "";

    if (sales.length === 0) {
        listStatus.textContent = "Der er ingen salg endnu.";
        return;
    }
    listStatus.textContent = "";

    sales.forEach(sale => {
        const row = document.createElement("tr");

        const cells = [
            formatDateTime(sale.saleTime),
            sale.productName,
            sale.quantity,
            formatPrice(sale.unitPrice),
            formatPrice(sale.total)
        ];

        cells.forEach(value => {
            const cell = document.createElement("td");
            cell.textContent = value;
            row.appendChild(cell);
        });

        saleList.appendChild(row);
    });
}

async function loadSales() {
    try {
        const response = await fetch("/kiosk/sales");
        if (!response.ok) {
            throw new Error();
        }
        renderSales(await response.json());
    } catch (e) {
        listStatus.textContent = "Kunne ikke hente salg.";
    }
}

saleForm.addEventListener("submit", async event => {
    event.preventDefault();
    saleStatus.textContent = "";
    saleButton.disabled = true;

    const request = {
        productId: productSelect.value ? Number(productSelect.value) : null,
        quantity: quantityInput.value ? Number(quantityInput.value) : null
    };

    try {
        const response = await fetch("/kiosk/sales", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            saleStatus.textContent = await readErrorMessage(response, "Salget kunne ikke registreres.");
            return;
        }

        const sale = await response.json();
        saleStatus.textContent = "Solgt: " + sale.quantity + " × " + sale.productName
            + " = " + formatPrice(sale.total);

        quantityInput.value = 1;
        updateTotal();
        await loadSales();
    } catch (e) {
        saleStatus.textContent = "Kunne ikke kontakte serveren.";
    } finally {
        saleButton.disabled = false;
    }
});

productSelect.addEventListener("change", updateTotal);
quantityInput.addEventListener("input", updateTotal);

loadProducts();
loadSales();