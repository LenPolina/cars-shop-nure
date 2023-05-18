export interface IOrder {
  id: number | null;
  user: string | null;
  orderDate: Date | null;
  orderStatus: string | null;
  orderTotalPrice: number;
  address: string;
  cars: number[];
}
