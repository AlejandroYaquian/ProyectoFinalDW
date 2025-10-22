let filas = 4, columnas = 5;
let tablero = document.getElementById("tablero");
let tableroConfig = document.getElementById("tableroConfig");
let listaMovimientos = document.getElementById("listaMovimientos");
let selectorPistas = document.getElementById("selectorPistas");

let robot = { fila: 3, columna: 0, direccion: 0 };
let movimientos = [];
let indicePista = 1;
let juegoTerminado = false;
let pista = [];

function precargarPistas() {
    const pistasPorDefecto = {
        "Pista 1": [[3,0],[2,0],[1,0],[1,1],[1,2],[2,2],[3,2]],
        "Pista 2": [[3,0],[3,1],[2,1],[1,1],[1,2],[2,2],[3,2]],
        "Pista 3": [[3,0],[2,0],[2,1],[2,2],[1,2],[1,3],[2,3]]
    };
    Object.keys(pistasPorDefecto).forEach(nombre => {
        if (!localStorage.getItem(nombre)) localStorage.setItem(nombre, JSON.stringify(pistasPorDefecto[nombre]));
    });
}

function cargarPistaAleatoria() {
    const nombres = Object.keys(localStorage);
    if (nombres.length < 3) return;
    const aleatorio = nombres[Math.floor(Math.random() * nombres.length)];
    pista = JSON.parse(localStorage.getItem(aleatorio));
}

function crearTablero(errorCelda = null) {
    tablero.innerHTML = "";
    tablero.style.gridTemplateColumns = `repeat(${columnas},1fr)`;
    tablero.style.gridTemplateRows = `repeat(${filas},1fr)`;

    for (let f = 0; f < filas; f++) {
        for (let c = 0; c < columnas; c++) {
            let celda = document.createElement("div");
            celda.classList.add("celda");
            if (pista.some(p => p[0] === f && p[1] === c)) celda.classList.add("pista");
            if (errorCelda && errorCelda[0] === f && errorCelda[1] === c) celda.classList.add("error");
            if (f === robot.fila && c === robot.columna) {
                let r = document.createElement("img");
                r.src = "img/robot.png";
                r.style.width = "80%";
                r.style.height = "80%";
                r.style.objectFit = "contain";
                celda.appendChild(r);
            }
            tablero.appendChild(celda);
        }
    }
}

function agregarMovimiento(tipo) {
    movimientos.push(tipo);
    let li = document.createElement("li");
    li.textContent = tipo.toUpperCase();
    listaMovimientos.appendChild(li);
}

function ejecutarMovimientos() {
    if (juegoTerminado) return;
    let i = 0;
    function paso() {
        if (i >= movimientos.length) {
            if (!juegoTerminado) mostrarMensaje("Intentalo de nuevo.");
            juegoTerminado = true;
            setTimeout(reiniciar, 2000);
            return;
        }
        let mov = movimientos[i];
        if (mov === "adelante") moverAdelante();
        else if (mov === "derecha") robot.direccion = (robot.direccion + 1) % 4;
        else if (mov === "izquierda") robot.direccion = (robot.direccion + 3) % 4;
        else if (mov === "bucle-inicio") {
            let fin = movimientos.indexOf("bucle-fin", i);
            if (fin !== -1) {
                let bloque = movimientos.slice(i + 1, fin);
                let repeticiones = 1;
                for (let r = 0; r < repeticiones; r++) movimientos.splice(fin + 1, 0, ...bloque);
            }
        }
        crearTablero();
        i++;
        if (!juegoTerminado) setTimeout(paso, 700);
    }
    paso();
}

function moverAdelante() {
    if (juegoTerminado) return;
    let nuevaFila = robot.fila;
    let nuevaColumna = robot.columna;
    if (robot.direccion === 0) nuevaFila--;
    else if (robot.direccion === 1) nuevaColumna++;
    else if (robot.direccion === 2) nuevaFila++;
    else if (robot.direccion === 3) nuevaColumna--;

    if (nuevaFila < 0 || nuevaFila >= filas || nuevaColumna < 0 || nuevaColumna >= columnas) {
        mostrarMensaje("Te saliste del tablero.");
        juegoTerminado = true;
        setTimeout(reiniciar, 2000);
        return;
    }

    if (pista[indicePista] && pista[indicePista][0] === nuevaFila && pista[indicePista][1] === nuevaColumna) indicePista++;
    else if (!pista.some(p => p[0] === nuevaFila && p[1] === nuevaColumna)) {
        mostrarMensaje("Te saliste de la pista.");
        robot.fila = nuevaFila;
        robot.columna = nuevaColumna;
        juegoTerminado = true;
        crearTablero([nuevaFila, nuevaColumna]);
        setTimeout(reiniciar, 2000);
        return;
    }

    robot.fila = nuevaFila;
    robot.columna = nuevaColumna;
    crearTablero();

    if (indicePista === pista.length) {
        mostrarMensaje("Felicitaciones, misión complida!");
        juegoTerminado = true;
    }
}

function reiniciar() {
    robot = { fila: 3, columna: 0, direccion: 0 };
    movimientos = [];
    indicePista = 1;
    juegoTerminado = false;
    listaMovimientos.innerHTML = "";
    crearTablero();
}

function mostrarMensaje(texto) {
    let mensaje = document.createElement("div");
    mensaje.textContent = texto;
    mensaje.classList.add("mensaje");
    document.body.appendChild(mensaje);
    setTimeout(() => mensaje.remove(), 1800);
}

let pistaConfig = [];
function abrirConfigurar() {
    document.getElementById("vistaJuego").classList.add("hidden");
    document.getElementById("vistaConfig").classList.remove("hidden");
    document.getElementById("panel").classList.add("hidden");
    crearTableroConfig();
}

function crearTableroConfig() {
    pistaConfig = [];
    tableroConfig.innerHTML = "";
    tableroConfig.style.gridTemplateColumns = `repeat(${columnas},1fr)`;
    tableroConfig.style.gridTemplateRows = `repeat(${filas},1fr)`;
    for (let f = 0; f < filas; f++) {
        for (let c = 0; c < columnas; c++) {
            let celda = document.createElement("div");
            celda.classList.add("celda");
            celda.onclick = () => {
                let index = pistaConfig.findIndex(p => p[0] === f && p[1] === c);
                if (index !== -1) pistaConfig.splice(index, 1);
                else pistaConfig.push([f, c]);
                actualizarTableroConfig();
            }
            tableroConfig.appendChild(celda);
        }
    }
}

function actualizarTableroConfig() {
    Array.from(tableroConfig.children).forEach((celda, i) => {
        let f = Math.floor(i / columnas);
        let c = i % columnas;
        if (pistaConfig.some(p => p[0] === f && p[1] === c)) celda.classList.add("pista");
        else celda.classList.remove("pista");
    });
}

function guardarPistaConfig() {
    let nombre = prompt("Nombre de la pista:");
    if (!nombre) return;
    pista = pistaConfig.slice();
    localStorage.setItem(nombre, JSON.stringify(pista));
    let option = document.createElement("option");
    option.value = nombre;
    option.textContent = nombre;
    selectorPistas.appendChild(option);
    mostrarMensaje(`Pista "${nombre}" guardada`);
}

function volverJuego() {
    document.getElementById("vistaConfig").classList.add("hidden");
    document.getElementById("vistaJuego").classList.remove("hidden");
    document.getElementById("panel").classList.remove("hidden");
    reiniciar();
}

function cargarPista(nombre) {
    let guardada = JSON.parse(localStorage.getItem(nombre));
    if (guardada) {
        pista = guardada;
        reiniciar();
    } else mostrarMensaje("No se encontró esa pista");
}

precargarPistas();
cargarPistaAleatoria();
Object.keys(localStorage).forEach(nombre => {
    let option = document.createElement("option");
    option.value = nombre;
    option.textContent = nombre;
    selectorPistas.appendChild(option);
});
crearTablero();
