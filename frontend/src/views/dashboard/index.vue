<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './admin'
import guestDashboard from './guest'

export default {
  name: 'Dashboard',
  components: { adminDashboard, guestDashboard },
  data() {
    return {
      currentRole: 'guestDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'role'
    ])
  },
  created() {
    switch (this.role) {
      case 0:
        this.currentRole = 'guestDashboard'
        break
      case 1:
        this.currentRole = 'adminDashboard'
        break
    }
  }
}
</script>
