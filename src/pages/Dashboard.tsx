import React from 'react';
import { useMetro } from '../context/MetroContext';
import { AdminLayout } from '../components/layout/AdminLayout';
import { MetroHeroCard } from '../components/dashboard/MetroHeroCard';
import { MetroDashboardCards } from '../components/dashboard/MetroDashboardCards';
import { MetroNetworkCanvas } from '../components/dashboard/MetroNetworkCanvas';
import { StationSalesTable } from '../components/dashboard/StationSalesTable';
import { QuickStationGrid } from '../components/dashboard/QuickStationGrid';
import { RecentGPSFeed } from '../components/dashboard/RecentGPSFeed';

export const Dashboard: React.FC = () => {
  return (
    <AdminLayout 
      title="Business Control Center" 
      subtitle="Metro+ Staff Network & Red Line Corridor Telemetry"
    >
      <div className="space-y-6">
        {/* 1. Large Hero Card matching Image 1 with Animated Red Metro Bus */}
        <MetroHeroCard />

        {/* 2. Three Cards matching Image 1 row below the Hero card */}
        <MetroDashboardCards />

        {/* 3. 24 Station Network Sales & Collections Breakdown */}
        <StationSalesTable />

        {/* 4. 24 Red Line Stations Corridor Radar Canvas */}
        <MetroNetworkCanvas />

        {/* 5. Operational Shortcuts Console */}
        <QuickStationGrid />

        {/* 6. Live CameraX GPS Captures */}
        <RecentGPSFeed />
      </div>
    </AdminLayout>
  );
};
