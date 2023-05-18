export interface IPayment {
  id: number | null;
  orderId: number | null;
  paymentDate: Date | null;
  paymentStatus: string | null;
  paymentTotalSum: number;
}
