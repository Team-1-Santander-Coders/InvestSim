<template>
    <div>
        <Toast ref="toast" />

        <div class="main-content" :class="{ 'blur-background': manageDialogVisible }">
            <h2>Lista de Ações</h2>

            <DataTable v-if="loading" :value="Array(5).fill({})" tableStyle="min-width: 100%; max-width: 90%;">
                <Column field="asset.ticker" header="Ticker" :style="{ minWidth: '100px' }">
                    <template #body>
                        <Skeleton width="80px" height="21px"></Skeleton>
                    </template>
                </Column>
                <Column field="quantity" header="Quantidade" :style="{ minWidth: '100px', textAlign: 'center' }">
                    <template #body>
                        <Skeleton width="60px" height="21px"></Skeleton>
                    </template>
                </Column>
                <Column field="currentValue" header="Valor Atual Total"
                    :style="{ minWidth: '150px', textAlign: 'center' }">
                    <template #body>
                        <Skeleton width="120px" height="21px"></Skeleton>
                    </template>
                </Column>
                <Column header="Ações" :style="{ minWidth: '120px', textAlign: 'center' }">
                    <template #body>
                        <Skeleton width="100px" height="31px"></Skeleton>
                    </template>
                </Column>
            </DataTable>

            <DataTable v-else-if="assetHoldings.length > 0" :value="assetHoldings" :paginator="true" :rows="15" tableStyle="min-width: 100%; max-width: 90%;"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                currentPageReportTemplate="Mostrando {first} a {last} de {totalRecords} ações"
                responsiveLayout="scroll">

                <Column field="asset.ticker" header="Ticker" :style="{ minWidth: '100px' }"></Column>
                <Column field="quantity" header="Quantidade" :style="{ minWidth: '100px', textAlign: 'center' }">
                </Column>
                <Column field="currentValue" header="Valor Atual Total"
                    :style="{ minWidth: '150px', textAlign: 'center' }">
                    <template #body="slotProps">
                        {{ "R$ " + (slotProps.data.currentValue?.toFixed(2) || '0.00') }}
                    </template>
                </Column>
                <Column header="Ações" :style="{ minWidth: '120px', textAlign: 'center' }">
                    <template #body="slotProps">
                        <Button label="Gerenciar" icon="pi pi-cog" class="p-button-sm"
                            @click="openManageDialog(slotProps.data)" />
                    </template>
                </Column>
            </DataTable>

            <p v-else class="no-assets-message">Nenhum ativo encontrado.</p>
        </div>

        <Dialog header="Gerenciar Ação" v-model:visible="manageDialogVisible" :modal="true" :closable="false"
            :dismissableMask="true" class="manage-dialog">

            <div v-if="selectedAsset?.asset" class="p-fluid manage-content">
                <div class="asset-info">
                    <h3>{{ selectedAsset.asset.ticker }}</h3>
                    <p class="current-price">Valor atual: R$ {{ selectedAsset.asset.currentPrice?.toFixed(2) || '0.00'
                        }}</p>
                </div>

                <div class="history-info">
                    <p>Valor de Compra: R$ {{ selectedAsset.buyValue?.toFixed(2) || '0.00' }}</p>
                    <p>Valorização:
                        <span
                            :class="{ 'positive': selectedAsset.valorizacao >= 0, 'negative': selectedAsset.valorizacao < 0 }">
                            {{ selectedAsset.valorizacao?.toFixed(2) || '0.00' }}%
                        </span>
                    </p>
                </div>

                <div class="date-compare">
                    <h2>Comparar Preço por Data</h2>

                    <div class="p-field">
                        <label for="compareDate">Selecione uma data:</label>
                        <DatePicker v-model="compareDate" dateFormat="dd/mm/yy" showIcon class="w-max" />
                    </div>

                    <Button label="Buscar Valor na Data" icon="pi pi-search" class="p-button-info"
                        @click="fetchPriceByDate" />

                    <p v-if="priceOnDate !== null" class="price-on-date">
                        Valor na data {{ formatDate(compareDate) }}: R$ {{ priceOnDate?.toFixed(2) || '0.00' }}
                    </p>

                    <Button v-if="priceOnDate !== null" label="Comparar com valor atual" icon="pi pi-chart-line"
                        class="p-button-secondary" @click="compareWithCurrentPrice" />

                    <p v-if="priceDifference !== null" class="price-difference">
                        Diferença: R$ {{ priceDifference?.toFixed(2) || '0.00' }}
                        <span v-if="selectedAsset?.asset?.currentPrice"
                            :class="{ 'positive': priceDifference >= 0, 'negative': priceDifference < 0 }">
                            ({{ priceDifference >= 0 ? '+' : '' }}{{ ((priceDifference /
                                selectedAsset.asset.currentPrice) * 100)?.toFixed(2) || '0.00' }}%)
                        </span>
                    </p>
                </div>

                <div class="sell-info">
                    <h4>Vender Ações</h4>
                    <div class="quantity-selector">
                        <label for="quantity">Quantidade:</label>
                        <InputNumber v-model="manageQuantity" :max="selectedAsset?.quantity || 0" :min="1"
                            class="p-inputtext-sm" />
                    </div>
                    <p class="sell-value">
                        Valor da Venda: <strong>R$ {{ ((selectedAsset?.asset?.currentPrice || 0) * (manageQuantity ||
                            0))?.toFixed(2) }}</strong>
                    </p>
                </div>

                <div class="actions">
                    <Button label="Confirmar Venda" icon="pi pi-check" class="p-button-success" @click="handleSell"
                        :disabled="!manageQuantity || manageQuantity < 1 || manageQuantity > (selectedAsset?.quantity || 0)" />
                    <Button label="Cancelar" icon="pi pi-times" class="p-button-text" @click="closeManageDialog" />
                </div>
            </div>
        </Dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import DataTable from 'primevue/datatable';
import DatePicker from 'primevue/datepicker';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputNumber from 'primevue/inputnumber';
import Toast from 'primevue/toast';
import Skeleton from 'primevue/skeleton';
import { useToast } from 'primevue/usetoast';

const loading = ref(true);
const assetHoldings = ref([]);
const manageDialogVisible = ref(false);
const manageQuantity = ref(1);
const selectedAsset = ref(null);
const compareDate = ref(null);
const priceOnDate = ref(null);
const priceDifference = ref(null);
const toast = useToast();

const formatDate = (dateString) => {
    const date = new Date(dateString);
    const formattedDate = date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
    return `${formattedDate}`;
};

const fetchPriceByDate = async () => {
    priceDifference.value = null;
    if (!compareDate.value) {
        toast.add({ severity: 'warn', summary: 'Atenção', detail: 'Selecione uma data para continuar.', life: 3000 });
        return;
    }

    const formattedDate = compareDate.value.toISOString().split('T')[0];

    try {
        const response = await fetch(`http://localhost:8080/asset/${selectedAsset.value.asset.id}?date=${formattedDate}`, {
            method: 'GET',
        });

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();
        priceOnDate.value = result[0].closePrice;
    } catch (error) {
        console.error('Erro ao buscar o valor do ativo na data informada:', error.message);
        toast.add({ severity: 'error', summary: 'Erro', detail: `Erro ao buscar o valor: ${error.message}`, life: 3000 });
    }
};

const compareWithCurrentPrice = () => {
    if (priceOnDate.value !== null && selectedAsset.value) {
        priceDifference.value = selectedAsset.value.asset.currentPrice - priceOnDate.value;
    }
};

const fetchAssets = async () => {
    loading.value = true;
    try {
        const getResponse = await fetch('http://localhost:8080/user/portfolio', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        });

        if (!getResponse.ok) {
            throw new Error(`Erro ${getResponse.status}: ${getResponse.statusText}`);
        }

        const responseText = await getResponse.text();
        if (!responseText) {
            throw new Error('Resposta vazia do servidor.');
        }

        const getData = JSON.parse(responseText);

        if (!getData.assetHoldings || getData.assetHoldings.length === 0) {
            throw new Error('Nenhum assetHoldings encontrado.');
        }

        assetHoldings.value = getData.assetHoldings
            .filter(holding => holding.quantity > 0)
            .map(holding => {
                const currentPrice = Number(holding.asset.currentPrice);
                const quantity = Number(holding.quantity);
                const buyValue = Number(holding.transcation.price / holding.transcation.quantity);
                const currentValue = currentPrice * quantity;
                const valorizacao = ((currentValue - (buyValue * quantity)) / (buyValue * quantity)) * 100;

                return {
                    ...holding,
                    buyValue,
                    currentValue,
                    valorizacao,
                    quantity,
                };
            });

    } catch (error) {
        console.error('Erro ao buscar assetHoldingList:', error.message);
        assetHoldings.value = [];
    } finally {
        setTimeout(() => {
            loading.value = false;
        }, 1000);
    }
};

const openManageDialog = (asset) => {
    selectedAsset.value = asset;
    manageQuantity.value = 1;
    compareDate.value = null;
    priceOnDate.value = null;
    priceDifference.value = null;
    manageDialogVisible.value = true;
};

const closeManageDialog = () => {
    manageDialogVisible.value = false;
    selectedAsset.value = null;
    manageQuantity.value = 1;
    compareDate.value = null;
    priceOnDate.value = null;
    priceDifference.value = null;
};

const handleSell = async () => {
    if (manageQuantity.value <= 0 || manageQuantity.value > selectedAsset.value?.quantity) {
        toast.add({ severity: 'warn', summary: 'Erro', detail: 'Quantidade inválida.', life: 3000 });
        return;
    }

    const requestData = {
        assetEntityId: selectedAsset.value.asset.id,
        quantity: manageQuantity.value
    };

    try {
        const response = await fetch('http://localhost:8080/user/sellassets', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            },
            body: JSON.stringify(requestData)
        });

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }

        const result = await response.json();
        
        if (response.ok) {
            closeManageDialog();
            fetchAssets();
            location.reload()
            toast.add({ severity: 'success', summary: 'Sucesso', detail: 'Ação vendida com sucesso.', life: 3000 });
        }
        
    } catch (error) {
        console.error('Erro ao vender o ativo:', error.message);
        toast.add({ severity: 'error', summary: 'Erro', detail: `Erro ao vender: ${error.message}`, life: 3000 });
    }
};

onMounted(fetchAssets);
</script>

<style scoped>
.main-content {
    transition: filter 0.3s ease;
}

.manage-dialog {
    max-width: 400px;
    margin: 0 auto;
}

.manage-content {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

.asset-info,
.sell-info,
.history-info,
.date-compare {
    text-align: center;
    background-color: #29292b;
    padding: 1rem;
    border-radius: 8px;
}

.date-compare {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.asset-info h3 {
    margin: 0;
    font-size: 1.5rem;
    color: #ffffff;
}

.current-price {
    font-size: 1.1rem;
    color: #ffffff;
    margin: 0.5rem 0 0;
}

.sell-info h4 {
    margin: 0 0 1rem;
    color: #ffffff;
}

.quantity-selector {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1rem;
}

.quantity-selector label {
    flex: 0 0 auto;
}

.quantity-selector .p-inputnumber {
    flex: 1;
}

.sell-value {
    font-size: 1.1rem;
    margin: 0;
}

.actions {
    display: flex;
    justify-content: space-between;
    gap: 1rem;
}

.actions .p-button {
    flex: 1;
}

.positive {
    color: #4CAF50;
}

.negative {
    color: #F44336;
}

.datatable-container {
    max-width: 100%;
    overflow-x: auto;
    padding: 1rem;
    position: relative;
}

h2 {
    text-align: center;
    margin-bottom: 1rem;
    font-size: 1.5rem;
}

.no-assets-message {
    text-align: center;
    font-size: 1.2rem;
    color: #888;
    margin-top: 2rem;
}

.p-datatable-wrapper {
    overflow-x: auto;
}

.p-datatable .p-datatable-header {
    text-align: center;
}

.date-compare {
    text-align: center;
    margin-top: 1.5rem;
}

.date-compare .price-on-date {
    margin-top: 1rem;
    font-size: 1.1rem;
    color: #ffffff;
}
</style>
