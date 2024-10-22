<template>
  <div class="w-[98%] h-[90vh] mx-auto my-4">
    <div class="card flex h-full">
      
      <AssetHoldingList class="w-full flex-grow"/>
      <Divider layout="vertical" class="mx-4"/>
      <div class="flex flex-col items-center w-full">
        <h2 class="text-center">Histórico de Transações</h2>
        <TransactioList class="w-full mb-4"/>
      </div>
     </div>
    </div>

</template>

<script setup>
import AssetHoldingList from "../components/AssetHoldingList.vue";
import TransactioList from "../components/TransactionList.vue";
import Divider from 'primevue/divider';
</script>

<style scoped>
.card {
  display: flex;
  gap: 1rem; 
}

.flex-grow {
  flex-grow: 1;
}
</style>
