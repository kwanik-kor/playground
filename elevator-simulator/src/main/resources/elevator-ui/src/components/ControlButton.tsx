import { motion } from 'motion/react';
import { ReactNode } from 'react';

interface ControlButtonProps {
  onClick: () => void;
  disabled: boolean;
  icon: ReactNode;
  active: boolean;
  label?: string;
}

export function ControlButton({ onClick, disabled, icon, active, label }: ControlButtonProps) {
  return (
    <motion.button
      onClick={onClick}
      disabled={disabled}
      className={`
        relative w-16 h-16 rounded-full
        transition-all duration-200
        ${disabled 
          ? 'cursor-not-allowed opacity-40' 
          : 'cursor-pointer hover:scale-105'
        }
      `}
      whileTap={!disabled ? { scale: 0.92 } : {}}
    >
      {/* Button outer ring - metal */}
      <div className="absolute inset-0 rounded-full bg-gradient-to-br from-[#a0a0a0] to-[#707070] shadow-md" />
      
      {/* Button face */}
      <div className={`
        absolute inset-1 rounded-full
        transition-all duration-200
        ${active 
          ? 'bg-gradient-to-br from-[#606060] to-[#404040]' 
          : 'bg-gradient-to-br from-[#d8d8d8] via-[#c0c0c0] to-[#a8a8a8]'
        }
      `}>
        {/* Brushed metal texture */}
        <div className="absolute inset-0 rounded-full brushed-button" />
        
        {/* Inner shadow for depth */}
        <div className={`
          absolute inset-0 rounded-full
          ${active ? 'button-pressed' : 'button-idle'}
        `} />
        
        {/* Highlight */}
        <div className="absolute inset-0 rounded-full bg-gradient-to-br from-white/40 via-transparent to-transparent" />
      </div>

      {/* Icon */}
      <div className={`
        relative z-10 flex items-center justify-center h-full
        transition-colors duration-200
        ${active ? 'text-white' : 'text-[#505050]'}
      `}>
        {icon}
      </div>

      {/* LED indicator light */}
      <div className={`
        absolute top-1 right-1 w-2 h-2 rounded-full
        transition-all duration-200
        ${active 
          ? 'bg-[#00ff00] led-active shadow-led-glow' 
          : 'bg-[#003300] border border-[#002200]'
        }
      `} />

      <style>{`
        .brushed-button {
          background-image: repeating-linear-gradient(
            45deg,
            transparent,
            transparent 1px,
            rgba(255, 255, 255, 0.04) 1px,
            rgba(255, 255, 255, 0.04) 2px
          );
        }

        .button-idle {
          box-shadow: 
            inset 0 1px 2px rgba(255, 255, 255, 0.8),
            inset 0 -2px 4px rgba(0, 0, 0, 0.2);
        }

        .button-pressed {
          box-shadow: 
            inset 0 2px 6px rgba(0, 0, 0, 0.5),
            inset 0 -1px 2px rgba(255, 255, 255, 0.1);
        }

        .led-active {
          animation: led-pulse 1.5s ease-in-out infinite;
        }

        .shadow-led-glow {
          box-shadow: 
            0 0 6px rgba(0, 255, 0, 0.9),
            0 0 10px rgba(0, 255, 0, 0.5);
        }

        @keyframes led-pulse {
          0%, 100% { opacity: 1; }
          50% { opacity: 0.7; }
        }
      `}</style>
    </motion.button>
  );
}