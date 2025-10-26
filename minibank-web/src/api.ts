export async function getHealth(): Promise<string> {
  const r = await fetch('/api/health');
  return r.text();
}

export async function getBalance(accountId: number): Promise<number> {
  const r = await fetch(`/api/accounts/${accountId}/balance`);
  return r.json();
}
