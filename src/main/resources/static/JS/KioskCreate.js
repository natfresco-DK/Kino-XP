const productForm = document.getElementById("productForm");
const productButton = productForm.querySelector("button[type='submit']");
const productNameInput = document.getElementById("productName");
const priceInput = document.getElementById("price");
const productList = document.getElementById("productList");

const createStatus = document.getElementById("createStatus");
const listStatus = document.getElementById("listStatus");

function formatPrice(value) {
    return Number(value).toLocaleString("da-DK", {
        style: "currency",
        currency: "DKK"
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

function renderProducts(products) {
    productList.innerHTML = "";

    if (products.length === 0) {
        listStatus.textContent = "Der er ingen varer endnu.";
        return;
    }
    listStatus.textContent = "";

    products.forEach(product => {
        const row = document.createElement("tr");

        const nameCell = document.createElement("td");
        nameCell.textContent = product.product;

        const priceCell = document.createElement("td");
        priceCell.textContent = formatPrice(product.price);

        row.appendChild(nameCell);
        row.appendChild(priceCell);
        productList.appendChild(row);
    });
}

async function loadProducts() {
    try {
        const response = await fetch("/kiosk/products");
        if (!response.ok) {
            throw new Error();
        }
        renderProducts(await response.json());
    } catch (e) {
        listStatus.textContent = "Kunne ikke hente varer.";
    }
}

productForm.addEventListener("submit", async event => {
    event.preventDefault();
    createStatus.textContent = "";
    productButton.disabled = true;

    const request = {
        product: productNameInput.value,
        price: priceInput.value
    };

    try {
        const response = await fetch("/kiosk/products", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            createStatus.textContent = await readErrorMessage(response, "Varen kunne ikke oprettes.");
            return;
        }

        const product = await response.json();
        createStatus.textContent = product.product + " er oprettet.";

        productForm.reset();
        await loadProducts();
    } catch (e) {
        createStatus.textContent = "Kunne ikke kontakte serveren.";
    } finally {
        productButton.disabled = false;
    }
});

loadProducts();