const apiBase = "http://localhost:8080/expenses/"; // adjust if your backend uses different path

function apiErrorText(res) {
  return res.text().then(text => text || res.statusText);
}

function formatDate(value) {
  if (!value) return '';
  const d = (typeof value === 'number' || !isNaN(Number(value))) ? new Date(Number(value)) : new Date(value);
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
}

function loadExpenses() {
  fetch(apiBase)
    .then(res => {
      if (!res.ok) throw new Error("Failed to load expenses");
      return res.json();
    })
    .then(list => {
      const tbody = document.getElementById("expense-table-body");
      if (!tbody) return;
      tbody.innerHTML = "";
      list.forEach(exp => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${exp.id ?? ''}</td>
          <td>${escapeHtml(exp.expenseName)}</td>
          <td>${escapeHtml(exp.expenseType)}</td>
          <td>${exp.price}</td>
          <td>${formatDate(exp.date)}</td>
          <td>
            <button onclick="goToEdit(${exp.id})">Edit</button>
            <button onclick="deleteExpense(${exp.id})">Delete</button>
          </td>
        `;
        tbody.appendChild(tr);
      });
    })
    .catch(err => alert("Could not load expenses: " + err.message));
}

function goToEdit(id) { location.href = `update.html?id=${id}`; }

function deleteExpense(id) {
  if (!confirm("Delete this expense?")) return;
  fetch(`${apiBase}/${id}`, { method: "DELETE" })
    .then(res => {
      if (!res.ok) return apiErrorText(res).then(t => { throw new Error(t) });
      loadExpenses();
    })
    .catch(err => alert("Delete failed: " + err.message));
}

if (document.getElementById("expense-form")) {
  const form = document.getElementById("expense-form");
  form.addEventListener("submit", e => {
    e.preventDefault();
    const payload = {
      expenseName: document.getElementById("expenseName").value.trim(),
      expenseType: document.getElementById("expenseType").value.trim(),
      price: parseFloat(document.getElementById("price").value),
      date: new Date(document.getElementById("date").value).getTime()
    };
    if (!payload.expenseName || !payload.expenseType || isNaN(payload.price) || payload.price < 1) {
      alert("Please provide valid: non-empty name/type and price >= 1");
      return;
    }
    fetch(apiBase, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) return apiErrorText(res).then(t => { throw new Error(t) });
        return res.json();
      })
      .then(() => {
        alert("Expense added");
        location.href = "index.html";
      })
      .catch(err => alert("Add failed: " + err.message));
  });
}

if (document.getElementById("update-form")) {
  const params = new URLSearchParams(window.location.search);
  const id = params.get("id");
  if (id) {
    fetch(`${apiBase}/${id}`)
      .then(res => {
        if (!res.ok) throw new Error("Failed to load expense");
        return res.json();
      })
      .then(data => {
        document.getElementById("expenseName").value = data.expenseName || "";
        document.getElementById("expenseType").value = data.expenseType || "";
        document.getElementById("price").value = data.price ?? "";
        document.getElementById("date").value = formatDate(data.date);
      });
  }
  document.getElementById("update-form").addEventListener("submit", e => {
    e.preventDefault();
    const payload = {
      id: parseInt(id),
      expenseName: document.getElementById("expenseName").value.trim(),
      expenseType: document.getElementById("expenseType").value.trim(),
      price: parseFloat(document.getElementById("price").value),
      date: new Date(document.getElementById("date").value).getTime()
    };
    if (!payload.expenseName || !payload.expenseType || isNaN(payload.price) || payload.price < 1) {
      alert("Please provide valid: non-empty name/type and price >= 1");
      return;
    }
    fetch(`${apiBase}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })
      .then(res => {
        if (!res.ok) return apiErrorText(res).then(t => { throw new Error(t) });
        return res.json();
      })
      .then(() => {
        alert("Updated");
        location.href = "index.html";
      })
      .catch(err => alert("Update failed: " + err.message));
  });
}

if (document.getElementById("expense-table-body")) { loadExpenses(); }

function escapeHtml(str) {
  if (!str) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}