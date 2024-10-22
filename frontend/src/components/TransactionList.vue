<template>
    <div class="card w-max">
        <DataTable :paginator="true" :rows="8" :value="transactions" tableStyle="min-width: 50rem"
            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
            currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} ações" responsiveLayout="scroll">
            <Column field="assetTicker" header="Ticker" sortable style="width: 25%">
                <template #body="slotProps">
                    {{ slotProps.data.assetTicker }}
                    <span v-if="companyNames[slotProps.data.assetTicker]">
                        - {{ companyNames[slotProps.data.assetTicker] }}
                    </span>
                </template>
            </Column>
            <Column field="quantity" header="Quantidade" sortable style="width: 25%">
                <template #body="slotProps">
                    {{ formatQuantity(slotProps.data.quantity) }}
                </template>
            </Column>
            <Column field="price" header="Preço" sortable style="width: 25%">
                <template #body="slotProps">
                    {{ formatPrice(slotProps.data.price) }}
                </template>
            </Column>
            <Column field="date" header="Data" sortable style="width: 25%">
                <template #body="slotProps">
                    {{ formatDate(slotProps.data.date) }}
                </template>
            </Column>
            <Column header="Tipo" style="width: 25%">
                <template #body="slotProps">
                    <Tag :icon="slotProps.data.type === 'BUY' ? 'pi pi-arrow-circle-down' : 'pi pi-arrow-circle-up'"
                        :severity="slotProps.data.type === 'BUY' ? 'success' : 'danger'"
                        :value="slotProps.data.type === 'BUY' ? 'Compra' : 'Venda'"></Tag>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import Tag from 'primevue/tag';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';

const transactions = ref([]);
const companyNames = ref({});

const fetchTransactions = async () => {
    let data = [];
    try {
        const response = await fetch('http://localhost:8080/user/transactions', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (!response.ok) {
            throw new Error('Erro ao buscar transações');
        }

        data = await response.json();
        return data;
    } catch (error) {
        console.error('Erro ao buscar transações:', error);
        return [];
    } finally {
        for (const asset of data) {
            await fetchAssetName(asset.assetTicker);
        }
    }
};

const fetchAssetName = async (ticker: string) => {
    if (!companyNames.value[ticker]) {
        const companyName = await getAssetNameByTicker(ticker);
        companyNames.value[ticker] = companyName;
    }
};

const getAssetNameByTicker = async (ticker: string) => {
    try {
        const response = await fetch(`http://localhost:8080/assetname/${ticker}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const companyName = await response.text();
        return companyName;
    } catch (error) {
        console.error('Error fetching asset name:', error);
        return 'Desconhecido';
    }
};

const formatPrice = (price: number) => {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(price);
};

const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    const formattedDate = date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
    const formattedTime = date.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    return `${formattedDate} ${formattedTime}`;
};

const formatQuantity = (quantity: number) => {
    return Number.isInteger(quantity) ? quantity : quantity.toFixed(2);
};

onMounted(async () => {
    transactions.value = await fetchTransactions();
});
</script>

<style>
h2 {
    text-align: center;
    margin-bottom: 1rem;
    font-size: 1.5rem;
}
</style>
