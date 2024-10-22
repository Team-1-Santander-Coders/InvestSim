<template>
  <div class="flex flex-col min-h-screen">
    <header>
      <Navbar />
    </header>
    <main class="flex-grow">
      <div class="flex-grow flex items-center justify-center py-6">
        <router-view />
      </div>
    </main>
    <footer class="mt-auto">
      <Card>
        <template #content>
          <div class="flex flex-row gap-4 items-center justify-center py-4">
            <div class="flex flex-row items-center justify-evenly space-x-4 select-none">
              <div class="w-[60%]">
                <p class="mb-3 footer-title">Alecsandro Schopf Auer Junior</p>
                <p class="footer-text mb-1">I'm a Fullstack Developer and DevOps enthusiast, focused on building
                  scalable
                  and
                  secure systems.</p>
                <p class="footer-text">I love using technology to create impactful solutions.
                </p>
              </div>
              <div>
              </div>
              <div class="w-[45%] justify-center flex flex-col">
                <div class="ml-auto gap-3">
                  <div id="text-footer" class="flex w-full items-start mb-2.5">
                    <p class="ml-2 text-2xl font-bold select-none">Repositório</p>
                  </div>
                  <div id="icons-footer" class="flex flex-row w-full">
                    <div class="icons-footer">
                      <a href="https://github.com/Team-1-Santander-Coders/InvestSim" target="_blank"
                        rel="noopener noreferrer">
                        <i class="github pi pi-github" style="font-size: 1.6rem;"></i>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <Divider />
          <p class="m-0 text-center text-xs select-none">&copy; 2024 Aleschopf. Todos os direitos reservados.</p>
        </template>
      </Card>
    </footer>
  </div>
  <ScrollTop />
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted} from 'vue';
import Card from 'primevue/card';
import Navbar from './components/Navbar.vue';
import ScrollTop from 'primevue/scrolltop';
import Divider from 'primevue/divider';

const darkMode = ref(document.documentElement.classList.contains('dark'));

const updateDarkMode = () => {
  darkMode.value = document.documentElement.classList.contains('dark');
};

let observer: MutationObserver;

onMounted(() => {
  updateDarkMode();

  observer = new MutationObserver(() => {
    updateDarkMode();
  });

  observer.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['class']
  });
});

onUnmounted(() => {
  if (observer) {
    observer.disconnect();
  }
});
</script>

<style>
.icons-footer a {
  padding-left: 8px;
}

i {
user-select: none;
}

i:hover {
  transition: color 0.25s ease;
}

.linkedin:hover {
  color: #1064c4;
}

.github:hover {
  color: #f04c34;
}

.linktree:hover {
  color: #42e660;
}

.instagram {
  background-image: linear-gradient(45deg,
      #f09433 0%,
      #e6683c 25%,
      #dc2743 50%,
      #cc2366 75%,
      #bc1888 100%);
  -webkit-background-clip: text;
  background-clip: text;
}

.instagram:hover {
  color: transparent;
}

.email:hover {
  color: #fe9700;
}

.p-dock {
  height: min-content !important;
  bottom: 0;
  margin: auto 0;
}

.dock-wrapper {
  position: fixed;
  top: 50%;
  transform: translateY(-50%);
  z-index: 100;
}

@media (max-width: 768px) {
  .p-dock {
      width: min-content !important;
      height: 2vh !important;
      position: fixed;
      bottom: 0;
      left: 0;
      right: 0;
      margin: 0 auto;
    }

  .p-dock-list {
    display: flex;
    justify-content: center;
  }

  .dock-wrapper {
    display: flex;
    justify-content: center;
    position: fixed;
    bottom: 0;
    width: 100%;
    height: min-content !important;
    top: 100%;
    transform: none;
  }
}

.dock-light-mode {
  --p-dock-background: rgba(51, 51, 51, 0.05) ;
  --p-dock-border-color: rgba(68, 68, 68, 0.5);
}
</style>