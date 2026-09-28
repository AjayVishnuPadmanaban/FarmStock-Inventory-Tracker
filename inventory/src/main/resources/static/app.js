const $ = id => document.getElementById(id);

function today() {
    // Use the local date, not UTC (toISOString() would show yesterday early morning in IST).
    const d = new Date();
    const pad = n => String(n).padStart(2, "0");
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}

function showMessage(text, error = false) {
    const el = $("message");
    el.textContent = text;
    el.className = error ? "error" : "success";
}

async function api(url, options = {}) {
    const response = await fetch(url, {
        headers: {"Content-Type": "application/json"},
        ...options
    });
    const data = await response.json().catch(() => ({}));
    if (!response.ok) {
        const details = data.errors ? Object.values(data.errors).join(", ") : "";
        throw new Error(details || data.message || "Request failed");
    }
    return data;
}

async function loadCrops() {
    const crops = await api("/api/crops");
    const options = '<option value="">Select crop</option>' +
        crops.map(c => `<option value="${c.id}">${escapeHtml(c.name)} (${escapeHtml(c.unit)})</option>`).join("");
    for (const id of ["harvestCrop", "saleCrop"]) {
        const select = $(id);
        const previous = select.value;
        select.innerHTML = options;
        select.value = previous;
    }
    $("totalCrops").textContent = crops.length;
}

async function loadStock() {
    const stock = await api("/api/stock");
    $("stockBody").innerHTML = stock.map(s => `
        <tr>
            <td>${escapeHtml(s.cropName)}</td>
            <td>${escapeHtml(s.unit)}</td>
            <td>${Number(s.totalHarvested).toFixed(2)}</td>
            <td>${Number(s.totalSold).toFixed(2)}</td>
            <td><strong>${Number(s.currentStock).toFixed(2)}</strong></td>
        </tr>
    `).join("") || '<tr><td colspan="5">No crops added yet.</td></tr>';

    $("totalHarvested").textContent =
        stock.reduce((sum, s) => sum + Number(s.totalHarvested), 0).toFixed(2);
    $("totalSold").textContent =
        stock.reduce((sum, s) => sum + Number(s.totalSold), 0).toFixed(2);
    $("currentStock").textContent =
        stock.reduce((sum, s) => sum + Number(s.currentStock), 0).toFixed(2);
}

async function loadDashboard() {
    try {
        await loadCrops();
        await loadStock();
        await loadRevenue();
    } catch (e) {
        showMessage(e.message, true);
    }
}

async function loadRevenue() {
    const start = $("startDate").value;
    const end = $("endDate").value;
    if (!start || !end) return;

    const report = await api(`/api/reports/revenue?startDate=${start}&endDate=${end}`);
    $("revenueBody").innerHTML = report.map(r => `
        <tr>
            <td>${escapeHtml(r.cropName)}</td>
            <td>₹ ${Number(r.revenue).toFixed(2)}</td>
        </tr>
    `).join("") || '<tr><td colspan="2">No sales for this date range.</td></tr>';
}

$("cropForm").addEventListener("submit", async e => {
    e.preventDefault();
    try {
        await api("/api/crops", {
            method: "POST",
            body: JSON.stringify({
                name: $("cropName").value,
                unit: $("cropUnit").value || "kg"
            })
        });
        e.target.reset();
        $("cropUnit").value = "kg";
        showMessage("Crop added successfully.");
        await loadDashboard();
    } catch (err) {
        showMessage(err.message, true);
    }
});

$("harvestForm").addEventListener("submit", async e => {
    e.preventDefault();
    try {
        await api("/api/harvests", {
            method: "POST",
            body: JSON.stringify({
                cropId: Number($("harvestCrop").value),
                quantity: Number($("harvestQty").value),
                harvestDate: $("harvestDate").value,
                notes: $("harvestNotes").value
            })
        });
        e.target.reset();
        $("harvestDate").value = today();
        showMessage("Harvest batch recorded successfully.");
        await loadDashboard();
    } catch (err) {
        showMessage(err.message, true);
    }
});

$("saleForm").addEventListener("submit", async e => {
    e.preventDefault();
    try {
        await api("/api/sales", {
            method: "POST",
            body: JSON.stringify({
                cropId: Number($("saleCrop").value),
                quantity: Number($("saleQty").value),
                pricePerUnit: Number($("salePrice").value),
                saleDate: $("saleDate").value,
                buyer: $("buyer").value
            })
        });
        e.target.reset();
        $("saleDate").value = today();
        showMessage("Sale recorded successfully.");
        await loadDashboard();
    } catch (err) {
        showMessage(err.message, true);
    }
});

$("revenueForm").addEventListener("submit", async e => {
    e.preventDefault();
    try {
        await loadRevenue();
        showMessage("Revenue report loaded.");
    } catch (err) {
        showMessage(err.message, true);
    }
});

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

$("harvestDate").value = today();
$("saleDate").value = today();
$("startDate").value = today().slice(0, 8) + "01";
$("endDate").value = today();

loadDashboard();
