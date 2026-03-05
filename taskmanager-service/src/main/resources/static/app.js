const state = {
    apiBase: "",
    logLines: [],
    startup: parseStartupOptions()
};

const dom = {
    apiBaseInput: document.getElementById("apiBase"),
    saveBaseButton: document.getElementById("saveBase"),
    runAllButton: document.getElementById("runAll"),
    statusBadge: document.getElementById("statusBadge"),
    userIdInput: document.getElementById("userIdInput"),
    categoryIdInput: document.getElementById("categoryIdInput"),
    taskIdInput: document.getElementById("taskIdInput"),
    usersOutput: document.getElementById("usersOutput"),
    categoriesOutput: document.getElementById("categoriesOutput"),
    tasksOutput: document.getElementById("tasksOutput"),
    logOutput: document.getElementById("logOutput")
};

initialize();

function initialize() {
    state.apiBase = getDefaultApiBase();
    dom.apiBaseInput.value = state.apiBase;
    setStatus("idle", "Ready");

    dom.saveBaseButton.addEventListener("click", () => {
        state.apiBase = sanitizeBase(dom.apiBaseInput.value);
        dom.apiBaseInput.value = state.apiBase;
        appendLog(`Base URL set to ${state.apiBase}`);
    });

    dom.runAllButton.addEventListener("click", runFullDemo);

    document.querySelectorAll("[data-action]").forEach((button) => {
        button.addEventListener("click", async () => {
            const action = button.getAttribute("data-action");
            await handleAction(action);
        });
    });

    appendLog("Client initialized");

    if (state.startup.autoRun) {
        runFullDemo().finally(() => applyPanelFocus(state.startup.panel));
    } else {
        applyPanelFocus(state.startup.panel);
    }
}

function parseStartupOptions() {
    const params = new URLSearchParams(window.location.search);
    return {
        autoRun: params.get("autorun") === "1",
        panel: params.get("panel")
    };
}

function getDefaultApiBase() {
    const origin = window.location.origin && window.location.origin !== "null"
        ? window.location.origin
        : "http://localhost:8080";
    const defaultBase = `${origin}/api`;
    return sanitizeBase(defaultBase);
}

function sanitizeBase(value) {
    return String(value || "").trim().replace(/\/+$/, "");
}

async function handleAction(action) {
    switch (action) {
        case "users-all":
            await showUsersAll();
            break;
        case "users-one":
            await showUserById(dom.userIdInput.value);
            break;
        case "categories-all":
            await showCategoriesAll();
            break;
        case "categories-one":
            await showCategoryById(dom.categoryIdInput.value);
            break;
        case "tasks-all":
            await showTasksAll();
            break;
        case "tasks-one":
            await showTaskById(dom.taskIdInput.value);
            break;
        case "tasks-names":
            await showTasksWithNames();
            break;
        default:
            appendLog(`Unknown action: ${action}`);
    }
}

async function runFullDemo() {
    setStatus("busy", "Running");
    appendLog("Starting full GET demo");

    try {
        const users = await showUsersAll();
        const userId = extractFirstId(users, "userId");
        if (userId != null) {
            dom.userIdInput.value = userId;
            await showUserById(userId);
        }

        const categories = await showCategoriesAll();
        const categoryId = extractFirstId(categories, "categoryId");
        if (categoryId != null) {
            dom.categoryIdInput.value = categoryId;
            await showCategoryById(categoryId);
        }

        const tasks = await showTasksAll();
        const taskId = extractFirstId(tasks, "taskId");
        if (taskId != null) {
            dom.taskIdInput.value = taskId;
            await showTaskById(taskId);
        }

        await showTasksWithNames();
        setStatus("success", "Complete");
        appendLog("Full GET demo complete");
    } catch (error) {
        setStatus("error", "Failed");
        appendLog(`Demo failed: ${error.message}`);
    }
}

function extractFirstId(items, key) {
    if (!Array.isArray(items) || items.length === 0) {
        return null;
    }
    const value = items[0][key];
    return typeof value === "number" ? value : null;
}

async function showUsersAll() {
    const payload = await requestJson("GET", "/users");
    setOutput(dom.usersOutput, payload.body);
    return payload.body;
}

async function showUserById(rawId) {
    const id = parseId(rawId, "user");
    const payload = await requestJson("GET", `/users/${id}`);
    setOutput(dom.usersOutput, payload.body);
    return payload.body;
}

async function showCategoriesAll() {
    const payload = await requestJson("GET", "/categories");
    setOutput(dom.categoriesOutput, payload.body);
    return payload.body;
}

async function showCategoryById(rawId) {
    const id = parseId(rawId, "category");
    const payload = await requestJson("GET", `/categories/${id}`);
    setOutput(dom.categoriesOutput, payload.body);
    return payload.body;
}

async function showTasksAll() {
    const payload = await requestJson("GET", "/tasks");
    setOutput(dom.tasksOutput, payload.body);
    return payload.body;
}

async function showTaskById(rawId) {
    const id = parseId(rawId, "task");
    const payload = await requestJson("GET", `/tasks/${id}`);
    setOutput(dom.tasksOutput, payload.body);
    return payload.body;
}

async function showTasksWithNames() {
    const payload = await requestJson("GET", "/tasks/with-names");
    setOutput(dom.tasksOutput, payload.body);
    return payload.body;
}

function parseId(rawId, label) {
    const id = Number.parseInt(String(rawId), 10);
    if (Number.isNaN(id) || id < 1) {
        throw new Error(`Enter a valid ${label} ID`);
    }
    return id;
}

function setOutput(element, data) {
    element.textContent = JSON.stringify(data, null, 2);
}

async function requestJson(method, path) {
    setStatus("busy", "Loading");
    const url = `${state.apiBase}${path}`;
    const start = Date.now();

    try {
        const response = await fetch(url, {
            method,
            headers: { Accept: "application/json" }
        });

        const elapsedMs = Date.now() - start;
        const text = await response.text();
        const body = tryParseJson(text);
        appendLog(`${method} ${path} -> HTTP ${response.status} (${elapsedMs} ms)`);

        if (!response.ok) {
            setStatus(response.status === 404 ? "warn" : "error", `HTTP ${response.status}`);
            throw new Error(`Request failed: ${response.status}`);
        }

        setStatus("success", `HTTP ${response.status}`);
        return { status: response.status, body };
    } catch (error) {
        appendLog(`${method} ${path} -> ERROR ${error.message}`);
        if (error instanceof TypeError) {
            setStatus("error", "Network error");
            throw new Error("Cannot reach API. Verify the API base URL and service status.");
        }
        throw error;
    }
}

function tryParseJson(text) {
    if (!text || !text.trim()) {
        return null;
    }
    try {
        return JSON.parse(text);
    } catch {
        return text;
    }
}

function setStatus(type, text) {
    dom.statusBadge.className = `status ${type}`;
    dom.statusBadge.textContent = text;
}

function appendLog(line) {
    const time = new Date().toLocaleTimeString();
    state.logLines.unshift(`[${time}] ${line}`);
    state.logLines = state.logLines.slice(0, 30);
    dom.logOutput.textContent = state.logLines.join("\n");
}

function applyPanelFocus(panel) {
    if (!panel) {
        return;
    }

    const panelMap = {
        users: "usersPanel",
        categories: "categoriesPanel",
        tasks: "tasksPanel",
        log: "logPanel"
    };

    const targetId = panelMap[String(panel).toLowerCase()];
    if (!targetId) {
        return;
    }

    const element = document.getElementById(targetId);
    if (element) {
        element.scrollIntoView({ behavior: "auto", block: "start" });
    }
}
