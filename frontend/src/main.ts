import { createApp } from 'vue'
import './assets/tailwind.css'
import router from './router'
import App from './App.vue'
import ToastService from 'primevue/toastservice';
import PrimeVue from 'primevue/config'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import InputText from 'primevue/inputtext'
import Message from 'primevue/message'
import Lara from '@primevue/themes/lara';
import 'primeicons/primeicons.css';
import '@fortawesome/fontawesome-free/css/all.css';
import { createPinia } from 'pinia'
import './plugins/axios'

const app = createApp(App)

const pinia = createPinia();
app.component('Button', Button)
app.component('Dialog', Dialog)
app.component('InputText', InputText)
app.component('Message', Message)
app.use(ToastService);
app.use(router)
app.use(pinia)
app.use(PrimeVue, {
    theme: {
        preset: Lara,
        options: {
            prefix: 'p',
            cssLayer: false,
            darkModeSelector: '.dark'
        },
    }
});

app.mount('#app')
