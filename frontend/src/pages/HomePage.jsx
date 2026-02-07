import { Link } from 'react-router-dom'

export default function HomePage() {
  const token = localStorage.getItem('token')
  return (
    <div style={{ padding: 48, textAlign: 'center' }}>
      <h1>Capstone Starter</h1>
      {token ? (
        <p><Link to="/users">Vào trang Users</Link></p>
      ) : (
        <p><Link to="/login">Đăng nhập</Link></p>
      )}
    </div>
  )
}
