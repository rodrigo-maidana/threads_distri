---

# Control de Hilos en Java

**Link del Video:** [Video de Youtube](https://youtu.be/GmsCIqxrXvg)

## Se añadieron los siguientes elementos:

### 1. Botones de control:
- **Iniciar Pelota 1 y Pelota 2:** Crean y ejecutan hilos que controlan cada pelota.
- **Parar Pelota 1:** Detiene permanentemente el hilo asociado a la primera pelota.
- **Dormir Pelota 2:** Suspende temporalmente el hilo de la segunda pelota.
- **Despertar Pelota 2:** Reactiva el hilo suspendido.

### 2. Estados de los hilos:
- Se demuestran los estados principales de un hilo:
  - `RUNNABLE`: Hilo en ejecución.
  - `WAITING`: Hilo suspendido.
  - `TIMED_WAITING`: Hilo pausado temporalmente.
  - `TERMINATED`: Hilo detenido.

### 3. Métodos del API de Java utilizados:
- **`Thread.sleep(ms)`**: Para pausar el hilo temporalmente (estado `TIMED_WAITING`).
- **`wait()` y `notify()`**: Para suspender (`WAITING`) y reanudar (`RUNNABLE`) un hilo.
- Uso de variables **`volatile`** para gestionar la concurrencia de manera segura.

### 4. Mensajes en consola:
- Se imprimen mensajes indicando el estado del hilo en tiempo real.

---
