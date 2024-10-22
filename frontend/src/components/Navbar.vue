<template>
    <div class="w-full flex items-center justify-center">
        <Menubar :model="items" class="w-[98%] rounded-lg shadow-md overflow-hidden">
            <template #end>
                <template v-if="!isAuthenticated">
                    <Button label="Entrar/Registrar" class="p-button-text mr-2" @click="openModal()" />
                </template>
                <template v-else>
                    <span class="mr-4 select-none">{{ userEmail }}</span>
                    <Button icon="pi pi-chart-bar" label="Acessar Dashboard" class="p-button-info mr-2"
                        @click="router.push('/user')" />
                    <Button icon="pi pi-sign-out" label="Logout" class="p-button-danger mr-2" @click="logout" />
                </template>
                <ToggleSwitch v-model="darkMode" @click="toggleDarkMode">
                    <template #handle="{ checked }">
                        <i :class="['pi', checked ? 'pi-moon' : 'pi-sun']"></i>
                    </template>
                </ToggleSwitch>
            </template>
        </Menubar>
        <AuthDialog v-model:visible="userModalVisible" />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import Menubar from 'primevue/menubar';
import ToggleSwitch from 'primevue/toggleswitch';
import Button from 'primevue/button';
import { useRouter } from 'vue-router';
import AuthDialog from './AuthDialog.vue';

const darkMode = ref(false);
const isAuthenticated = ref(false);
const userEmail = ref('');
const userModalVisible = ref(false);

const router = useRouter();

const toggleDarkMode = () => {
    document.documentElement.classList.toggle('dark', darkMode.value);
};

const items = ref([
    {
        label: 'Home',
        command: () => router.push('/'),
    },
]);

const openModal = () => {
    userModalVisible.value = true;
};

const closeModal = () => {
    userModalVisible.value = false;
};

const logout = () => {
    location.reload();
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    isAuthenticated.value = false;
    userEmail.value = '';
};

onMounted(() => {
    const savedDarkMode = localStorage.getItem('darkMode');
    darkMode.value = savedDarkMode === 'true';
    toggleDarkMode();

    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (token && user) {
        isAuthenticated.value = true;
        userEmail.value = user;
    }
});

watch(darkMode, (newValue) => {
    localStorage.setItem('darkMode', newValue.toString());
    toggleDarkMode();
});
</script>

<style scoped>
</style>
