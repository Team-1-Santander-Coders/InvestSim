import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || null,
        user: localStorage.getItem('user') || null
    }),

    getters: {
        isAuthenticated: (state) => !!state.token,
        getUser: (state) => state.user
    },

    actions: {
        async login(email: string, password: string) {
            try {
                const response = await axios.post('http://localhost:8080/login', {
                    email,
                    password
                })

                const { token, email: userEmail } = response.data

                this.token = token
                this.user = userEmail

                localStorage.setItem('token', token)
                localStorage.setItem('user', userEmail)

                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

                return Promise.resolve(response)
            } catch (error) {
                return Promise.reject(error)
            }
        },

        async register(document: string, email: string, password: string) {
            try {
                const response = await axios.post('http://localhost:8080/register', {
                    document,
                    email,
                    password
                })

                const { token, email: userEmail } = response.data

                this.token = token
                this.user = userEmail

                localStorage.setItem('token', token)
                localStorage.setItem('user', userEmail)

                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`

                return Promise.resolve(response)
            } catch (error) {
                return Promise.reject(error)
            }
        },

        logout() {
            this.token = null
            this.user = null
            localStorage.removeItem('token')
            localStorage.removeItem('user')
            delete axios.defaults.headers.common['Authorization']
        }
    }
})