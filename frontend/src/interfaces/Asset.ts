export interface Asset {
    id: number;
    ticker: string;
    currentPrice: number;
    dailyChange: number;
    openPrice: number;
    closePrice: number;
    highPrice: number;
    lowPrice: number;
    volume: number;
}