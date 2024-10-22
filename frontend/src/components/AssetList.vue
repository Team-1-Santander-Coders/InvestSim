<template>
    <div class="asset-table h-full">
        <DataTable v-model:expandedRows="expandedRows" :value="displayedAssets" :paginator="true" :rows="10"
            :totalRecords="totalRecords" :lazy="true" :loading="isLoading" @page="onPage" dataKey="id"
            @rowExpand="onRowExpand" @rowCollapse="onRowCollapse" tableStyle="min-width: 60rem">


            <template #header>
                <div class="flex flex-wrap justify-end gap-2">
                    <IconField>
                        <InputIcon>
                            <i class="pi pi-search" />
                        </InputIcon>
                        <InputText v-model="filters.global" placeholder="Pesquise a ação que deseja"
                            @input="onSearch" />
                    </IconField>
                    <Button text icon="pi pi-minus" label="Fechar Todas" @click="collapseAll" />
                </div>
            </template>

            <Column expander style="width: 5rem" />
            <Column field="ticker" header="Ação">
                <template #body="slotProps">
                    <div class="flex flex-row gap-1.5 items-center">
                        <img :src="`./logos/${slotProps.data.ticker}.png`" class="w-8 h-auto rounded-md" />
                        <span>{{ slotProps.data.ticker }}</span>
                        <span v-if="companyNames[slotProps.data.ticker]">
                            - {{ companyNames[slotProps.data.ticker] }}
                        </span>
                    </div>
                </template>
            </Column>
            <Column header="Preço Atual">
                <template #body="slotProps">
                    <template v-if="assetCurrentData[slotProps.data.id]">
                        $ {{ formatPrice(assetCurrentData[slotProps.data.id].currentPrice) }}
                    </template>
                    <Skeleton v-else width="5rem" height="1.5rem" />
                </template>
            </Column>
            <Column header="Variação Diária">
                <template #body="slotProps">
                    <template v-if="assetCurrentData[slotProps.data.id]">
                        $ {{ formatPrice(assetCurrentData[slotProps.data.id].dailyChange) }}
                    </template>
                    <Skeleton v-else width="5rem" height="1.5rem" />
                </template>
            </Column>
            <Column header="Fechamento">
                <template #body="slotProps">
                    <template v-if="assetCurrentData[slotProps.data.id]">
                        $ {{ formatPrice(assetCurrentData[slotProps.data.id].closePrice) }}
                    </template>
                    <Skeleton v-else width="5rem" height="1.5rem" />
                </template>
            </Column>
            <Column header="Volume">
                <template #body="slotProps">
                    <template v-if="assetCurrentData[slotProps.data.id]">
                        {{ assetCurrentData[slotProps.data.id].volume }}
                    </template>
                    <Skeleton v-else width="5rem" height="1.5rem" />
                </template>
            </Column>
            <Column v-if="isAuthenticated" header="Comprar">
                <template #body="slotProps">
                    <Button icon="pi pi-wallet" label="Comprar ação" @click="openBuyModal(slotProps.data)" />
                </template>
            </Column>

            <template #expansion="slotProps">
                <div class="p-4">
                    <h4 class="select-none font-bold">Dados históricos de {{ slotProps.data.ticker }}</h4>
                    <div v-if="!chartData[slotProps.data.id]" class="h-[30rem] flex items-center justify-center">
                        <Skeleton class="w-full h-full" />
                    </div>
                    <Chart v-else type="line" :data="chartData[slotProps.data.id]"
                        :options="chartOptions[slotProps.data.id]" class="h-[30rem]" />
                </div>
            </template>
        </DataTable>

        <Dialog v-model:visible="showBuyModal" header="Comprar Ação" :visible="showBuyModal" modal>
            <div class="gap-2">
                <h2><strong>Ação:</strong> {{ selectedAsset.ticker }}</h2>
                <p class="mt-2"><strong>Preço Atual:</strong> $ {{ formatPrice(assetCurrentData[selectedAsset.id].closePrice) }}</p>
                <div class="mt-2">
                    <span><strong>Quantidade: </strong> </span>
                    <InputNumber v-model="quantity" @input="updateQuantity" min="1" />
                </div>
            </div>
            <template #footer>
                <Button icon="pi pi-money-bill" label="Comprar" @click="buyAsset" />
                <Button icon="pi pi-times-circle" label="Cancelar" @click="closeBuyModal" class="p-button-secondary" />
            </template>
        </Dialog>
        <Toast ref="toast" />
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import Card from 'primevue/card';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Chart from 'primevue/chart';
import InputText from 'primevue/inputtext';
import InputIcon from 'primevue/inputicon';
import IconField from 'primevue/iconfield';
import Dialog from 'primevue/dialog';
import InputNumber from 'primevue/inputnumber';
import Toast from 'primevue/toast';
import Skeleton from 'primevue/skeleton';
import { format } from 'date-fns';

const displayedAssets = ref([]);
const totalRecords = ref(0);
const isLoading = ref(true);
const assetCurrentData = ref({});
const expandedRows = ref({});
const chartData = ref({});
const chartOptions = ref({});
const filters = ref({ global: '' });
const currentPage = ref(0);
const showBuyModal = ref(false);
const selectedAsset = ref({});
const quantity = ref(1);
const toast = ref(null);
const isAuthenticated = ref(false);
const companyNames = ref({});

const debounce = (fn, delay) => {
    let timeoutId;
    return (...args) => {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => fn.apply(this, args), delay);
    };
};

const onSearch = debounce(() => {
    currentPage.value = 0;
    loadPagedAssets();
}, 300);

const loadPagedAssets = async () => {
    isLoading.value = true;
    try {
        const response = await fetch('http://localhost:8080/assets');
        const allAssets = await response.json();

        let filteredAssets = allAssets;
        if (filters.value.global) {
            const searchTerm = filters.value.global.toLowerCase();
            filteredAssets = allAssets.filter(asset =>
                asset.ticker.toLowerCase().includes(searchTerm) ||
                (companyNames.value[asset.ticker] || '').toLowerCase().includes(searchTerm)
            );
        }

        totalRecords.value = filteredAssets.length;

        const start = currentPage.value * 10;
        const end = start + 10;
        displayedAssets.value = filteredAssets.slice(start, end);

        await loadCurrentData();
    } catch (error) {
        console.error('Error loading assets:', error);
    } finally {
        isLoading.value = false;
    }
};

const loadCurrentData = async () => {
    const today = format(new Date(), 'yyyy-MM-dd');

    const promises = displayedAssets.value.map(async (asset) => {
        try {
            const response = await fetch(`http://localhost:8080/asset/${asset.id}?date=${today}`);
            const data = await response.json();
            
            assetCurrentData.value = {
                ...assetCurrentData.value,
                [asset.id]: {
                    currentPrice: data[0].openPrice,
                    dailyChange: data[0].highPrice - data[0].lowPrice,
                    closePrice: data[0].closePrice,
                    volume: data[0].volume
                }
            };
        } catch (error) {
            console.error(`Error loading current data for asset ${asset.id}:`, error);
        }
    });

    await Promise.all(promises);
};


const onPage = (event) => {
    currentPage.value = event.page;
    loadPagedAssets();
};

const setChartData = (data) => {
    const documentStyle = getComputedStyle(document.documentElement);
    const labels = data.map(item => format(new Date(item.date), 'dd/MM/yyyy'));
    const prices = data.map(item => item.closePrice);
    const volumes = data.map(item => item.volume);
    return {
        labels,
        datasets: [{
            label: 'Preço de Fechamento',
            data: prices,
            borderColor: '#2196F3',
            yAxisID: 'y',
            tension: 0.4,
            fill: false
            },
            {
                label: 'Volume de Negociações',
                data: volumes,
                yAxisID: 'y1',
                borderColor: documentStyle.getPropertyValue('--p-gray-500'),
                tension: 0.4,
                fill: false
            }
    ]
    };
};

const setChartOptions = () => {
    return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'bottom'
            },
            tooltip: {
                mode: 'index',
                intersect: false
            }
        },
        scales: {
            y: {
                beginAtZero: false,
                ticks: {
                    callback: (value) => `$ ${value.toFixed(2)}`
                }
            }
        },
        interaction: {
            intersect: false
        }
    };
};

const fetchAssetDetails = async (id) => {
    try {
        const response = await fetch(`http://localhost:8080/asset/${id}`);
        const data = await response.json();
        chartData.value[id] = setChartData(data);
        chartOptions.value[id] = setChartOptions();
    } catch (error) {
        console.error('Error fetching asset details:', error);
    }
};

const onRowExpand = (event) => {
    const assetId = event.data.id;
    fetchAssetDetails(assetId);
};

const onRowCollapse = (event) => {
    const assetId = event.data.id;
    delete chartData.value[assetId];
    delete chartOptions.value[assetId];
};

const collapseAll = () => {
    expandedRows.value = {};
};

const openBuyModal = (asset) => {
    selectedAsset.value = asset;
    quantity.value = 1;
    showBuyModal.value = true;
};

const closeBuyModal = () => {
    showBuyModal.value = false;
};

const updateQuantity = (value) => {
    quantity.value = Number(value) || 1;
};

const formatPrice = (price) => {
    return price ? price.toFixed(2).toString().replace('.', ',') : '0,00';
};

const buyAsset = async () => {
    const payload = {
        assetEntityId: selectedAsset.value.id,
        quantity: quantity.value
    };

    try {
        const response = await fetch('http://localhost:8080/user/buyasset', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error('Erro ao comprar ação');
        }

        const result = await response.json();
        toast.value.add({
            severity: 'success',
            summary: 'Compra realizada',
            detail: `Você comprou ${quantity.value} ações de ${selectedAsset.value.ticker}`
        });
    } catch (error) {
        console.error(error);
        toast.value.add({
            severity: 'error',
            summary: 'Erro',
            detail: 'Falha ao realizar a compra'
        });
    } finally {
        closeBuyModal();
    }
};

onMounted(() => {
    loadPagedAssets();
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    if (token && user) {
        isAuthenticated.value = true;
    }
});
</script>

<style scoped>
.asset-table {
    padding: 1rem;
}
</style>