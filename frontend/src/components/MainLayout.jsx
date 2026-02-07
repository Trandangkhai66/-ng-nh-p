import { Outlet } from 'react-router-dom'
import { Link } from 'react-router-dom'
import { logout } from '../features/auth/auth.api'

export default function MainLayout() {
  return (
    <div>
      <header style={{ padding: '12px 24px', borderBottom: '1px solid #ddd', display: 'flex', gap: 16, alignItems: 'center' }}>
        <Link to="/users">Users</Link>
        <button onClick={logout} style={{ marginLeft: 'auto' }}>Logout</button>
      </header>
      <Outlet />
    </div>
  )
}
