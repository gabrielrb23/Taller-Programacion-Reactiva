const API_BASE_URL = 'http://localhost:8080/api';

class ApiService {
    // Métodos para estudiantes
    static getEstudiantes() {
        return fetch(`${API_BASE_URL}/estudiantes`)
            .then(response => response.json());
    }

    static createEstudiante(estudiante) {
        return fetch(`${API_BASE_URL}/estudiantes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(estudiante)
        }).then(response => response.json());
    }

    // Métodos para materias
    static getMaterias() {
        return fetch(`${API_BASE_URL}/materias`)
            .then(response => response.json());
    }

    static createMateria(materia) {
        return fetch(`${API_BASE_URL}/materias`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(materia)
        }).then(response => response.json());
    }

    // Métodos para notas
    static getNotas(materiaId, estudianteId) {
        return fetch(`${API_BASE_URL}/notas?materiaId=${materiaId}&estudianteId=${estudianteId}`)
            .then(response => response.json());
    }

    static createNota(nota) {
        return fetch(`${API_BASE_URL}/notas`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(nota)
        }).then(response => response.json());
    }

    static getPromedio(materiaId, estudianteId) {
        return fetch(`${API_BASE_URL}/notas/promedio?materiaId=${materiaId}&estudianteId=${estudianteId}`)
            .then(response => response.json());
    }
}